package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.component.IComponent;
import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.RootModel;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFlowBuilder implements FlowBuilder, InitializingBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ApplicationContext applicationContext;

    @Override
    public void build() {
        List<RootModel> resources = this.loadResources();
        if (resources.isEmpty()) {
            this.logger.info("No resources found. Skipping build.");
            return;
        }
        resources.forEach(this::build);
    }

    @Override
    public void build(String identity) {
        RootModel rootModel = this.loadResource(identity);
        this.build(rootModel);
    }

    /**
     * 加载所有{@link Job}的解析模板{@link RootModel}
     *
     * @return {@link RootModel}集合
     */
    protected abstract List<RootModel> loadResources();

    /**
     * 加载指定{@link Job}的解析模板{@link RootModel}
     *
     * @return {@link RootModel}
     */
    protected abstract RootModel loadResource(String identity);

    private void build(RootModel rootModel) {
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

    /**
     * 根据{@link  RootModel}构建{@link Job}
     *
     * @param rootModel {@link RootModel}实例
     * @return {@link Job}
     */
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

    public static void buildComponents(List<? extends ComponentModel> list,
                                       AtomicReference<Object> reference, ApplicationContext applicationContext) {
        list.forEach(model -> applicationContext.getBeansOfType(IComponent.class).values().stream()
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
