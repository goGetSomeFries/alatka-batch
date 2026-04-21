package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.component.IComponent;
import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.GroupAwareJob;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
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
        resources.forEach(this::doBuild);
    }

    @Override
    public void build(String identity) {
        RootModel rootModel = this.loadResource(identity);
        this.doBuild(rootModel);
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

    private void doBuild(RootModel rootModel) {
        Validator.validate(rootModel);
        if (!rootModel.isEnabled()) {
            this.logger.warn("model '{}' is disabled. Skipping build...", rootModel.getName());
            return;
        }
        Job job = this.buildJob(rootModel);
        GroupAwareJob groupAwareJob = new GroupAwareJob(rootModel.getGroup(), job);
        ReferenceJobFactory jobFactory = new ReferenceJobFactory(groupAwareJob);
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
    private Job buildJob(RootModel rootModel) {
        JobRepository jobRepository = applicationContext.getBean(JobRepository.class);
        JobBuilder jobBuilder = new JobBuilder(rootModel.getName(), jobRepository);
        AtomicReference<Object> reference = new AtomicReference<>(jobBuilder);

        buildComponents(rootModel.getSteps(), reference, applicationContext);
        Job job = IComponent.build(reference.get());
        this.postJob(rootModel, (AbstractJob) job);
        return job;
    }

    private void postJob(RootModel model, AbstractJob job) {
        if (model.getListeners() != null && !model.getListeners().isEmpty()) {
            JobExecutionListener[] listeners = model.getListeners().stream()
                    .map(applicationContext::getBean)
                    .map(bean -> {
                        if (bean instanceof JobExecutionListener) {
                            return JobExecutionListener.class.cast(bean);
                        }
                        throw new IllegalArgumentException("listener [" + bean.getClass().getName() + "] is not a JobExecutionListener");
                    }).toArray(JobExecutionListener[]::new);
            job.setJobExecutionListeners(listeners);
        }
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
