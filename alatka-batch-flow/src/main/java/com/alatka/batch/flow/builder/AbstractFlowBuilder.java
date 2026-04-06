package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.component.IComponent;
import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.JsonUtil;
import com.alatka.batch.infra.util.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFlowBuilder implements FlowBuilder, InitializingBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ROOT_NAME = "alatka.batch.flow";

    private ApplicationContext applicationContext;

    @Override
    public void build() {
        List<Resource> resources = this.loadResources();
        if (resources.isEmpty()) {
            this.logger.info("No resources found. Skipping build.");
            return;
        }
        resources.forEach(this::build);
    }

    @Override
    public void build(String identity) {
        Resource resource = this.loadResource(identity);
        this.build(resource);
    }

    protected abstract List<Resource> loadResources();

    protected abstract Resource loadResource(String identity);

    private void build(Resource resource) {
        RootModel rootModel = YamlUtil.loadYaml(resource, ROOT_NAME, RootModel.class);
        if (!rootModel.isEnabled()) {
            this.logger.warn("model '{}' is disabled. Skipping build...", rootModel.getName());
            return;
        }
        Job job = this.doBuild(rootModel);
        ReferenceJobFactory jobFactory = new ReferenceJobFactory(job);
        JobRegistry jobRegistry = applicationContext.getBean(JobRegistry.class);
        try {
            jobRegistry.unregister(jobFactory.getJobName());
            jobRegistry.register(jobFactory);
        } catch (DuplicateJobException e) {
            throw new RuntimeException(e);
        }
    }

    private Job doBuild(RootModel rootModel) {
        JobBuilderFactory jobBuilderFactory = applicationContext.getBean(JobBuilderFactory.class);
        JobBuilder jobBuilder = jobBuilderFactory.get(rootModel.getName());
        AtomicReference<Object> reference = new AtomicReference<>(jobBuilder);

        buildComponents(rootModel.getSteps(), reference, applicationContext);

        IComponent iComponent = applicationContext.getBeansOfType(IComponent.class).values().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can not found bean of " + IComponent.class.getSimpleName()));
        return iComponent.build(reference.get());
    }

    public static void buildComponents(List<Map<RootModel.Type, Map<String, Object>>> list,
                                       AtomicReference<Object> reference, ApplicationContext applicationContext) {
        list.stream().flatMap(map -> map.entrySet().stream()
                        .map(entry -> JsonUtil.convertToObject(entry.getValue(), entry.getKey().getClazz())))
                .forEach(model -> applicationContext.getBeansOfType(IComponent.class).values().stream()
                        .filter(component -> component.matched(model))
                        .findFirst()
                        .ifPresent(component -> {
                            Object builder = component.join(model, reference.get());
                            reference.set(builder);
                        })
                );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.build();
    }
}
