package com.alatka.batch.example.listener;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleCounter;
import io.opentelemetry.api.metrics.Meter;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class OpenTelemetryAgentCpuMonitor {

    private static final AttributeKey<String> THREAD_NAME = AttributeKey.stringKey("thread.name");

    private final ThreadMXBean threadMXBean;
    private final DoubleCounter agentCpuCounter;
    private final Map<Long, Long> lastCpuTimeNs = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public OpenTelemetryAgentCpuMonitor() {
        this.threadMXBean = ManagementFactory.getThreadMXBean();

        if (!threadMXBean.isThreadCpuTimeSupported()) {
            throw new IllegalStateException("当前 JVM 不支持 Thread CPU Time");
        }
        threadMXBean.setThreadCpuTimeEnabled(true);

        Meter meter = GlobalOpenTelemetry.getMeter("opentelemetry.agent.cpu.monitor");
        this.agentCpuCounter = meter.counterBuilder("otel.agent.thread.cpu.time")
                .setDescription("CPU time consumed by OpenTelemetry Java Agent related threads")
                .setUnit("s")
                .ofDoubles()
                .build();

        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "otel-agent-cpu-monitor");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * 启动监控
     *
     * @param intervalSeconds 采样间隔（建议 10~30 秒）
     */
    public void start(long intervalSeconds) {
        if (!started.compareAndSet(false, true)) {
            System.out.println("[AgentCpuMonitor] already started, ignore");
            return;
        }

        scheduler.scheduleAtFixedRate(this::sample, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
        System.out.println("[AgentCpuMonitor] started, interval=" + intervalSeconds + "s");
    }

    private void sample() {
        try {
            long[] ids = threadMXBean.getAllThreadIds();
            ThreadInfo[] infos = threadMXBean.getThreadInfo(ids);

            for (int i = 0; i < ids.length; i++) {
                long id = ids[i];
                ThreadInfo info = infos[i];
                if (info == null) {
                    continue;
                }

                String name = info.getThreadName();
                if (!isAgentThread(name)) {
                    continue;
                }

                long currentNs = threadMXBean.getThreadCpuTime(id);
                if (currentNs < 0) {
                    continue;
                }

                Long lastNs = lastCpuTimeNs.put(id, currentNs);
                if (lastNs != null && currentNs >= lastNs) {
                    double deltaSec = (currentNs - lastNs) / 1_000_000_000.0;
                    agentCpuCounter.add(deltaSec, Attributes.of(THREAD_NAME, name));
                }
            }

            // 清理已死亡线程
            lastCpuTimeNs.keySet().removeIf(id -> threadMXBean.getThreadInfo(id) == null);

        } catch (Throwable t) {
            System.err.println("[AgentCpuMonitor] sample error: " + t.getMessage());
        }
    }

    private boolean isAgentThread(String name) {
        if (name == null) {
            return false;
        }
        String n = name.toLowerCase();
        return n.contains("opentelemetry")
                || n.contains("batchspanprocessor")
                || n.contains("batchlogrecordprocessor")
                || n.contains("otlp")
                || n.contains("metricreader")
                || n.contains("exporter")
                || (n.contains("okhttp") && n.contains("dispatcher"));
    }

    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            scheduler.shutdownNow();
        }
    }
}