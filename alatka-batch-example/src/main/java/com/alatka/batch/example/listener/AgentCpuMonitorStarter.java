package com.alatka.batch.example.listener;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class AgentCpuMonitorStarter {

    private final OpenTelemetryAgentCpuMonitor monitor = new OpenTelemetryAgentCpuMonitor();

    @PostConstruct
    public void start() {
        monitor.start(10);
    }

    @PreDestroy
    public void stop() {
        monitor.shutdown();
    }
}