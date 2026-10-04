package com.alatka.batch.monitor.jobstep;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;

public class JobParametersExtractorWrapper extends DefaultJobParametersExtractor {

    private JobParametersExtractor jobParametersExtractor;

    public static final String PARENT_STEP_EXECUTION_ID = "alatka.parent.stepExecutionId";

    public static final String CURRENT_JOB_NAME = "alatka.current.jobName";

    public JobParametersExtractorWrapper(JobParametersExtractor jobParametersExtractor) {
        this.jobParametersExtractor = jobParametersExtractor;
    }

    @Override
    public JobParameters getJobParameters(Job job, StepExecution stepExecution) {
        JobParameters jobParameters = jobParametersExtractor.getJobParameters(job, stepExecution);

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addJobParameters(jobParameters);
        builder.addLong(PARENT_STEP_EXECUTION_ID, stepExecution.getId(), false);
        builder.addString(CURRENT_JOB_NAME, job.getName(), false);

        return builder.toJobParameters();
    }
}
