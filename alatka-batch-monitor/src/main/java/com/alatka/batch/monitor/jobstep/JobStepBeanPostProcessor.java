package com.alatka.batch.monitor.jobstep;

import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class JobStepBeanPostProcessor implements BeanPostProcessor {

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JobStep jobStep) {
            Field field = ReflectionUtils.findField(JobStep.class, "jobParametersExtractor");
            ReflectionUtils.makeAccessible(field);
            JobParametersExtractor originExtractor = (JobParametersExtractor) ReflectionUtils.getField(field, bean);
            jobStep.setJobParametersExtractor(new JobParametersExtractorWrapper(originExtractor));
        }
        return bean;
    }
}
