package com.alatka.batch.monitor.admin.service;

import com.alatka.batch.monitor.admin.entity.JobExecution;
import com.alatka.batch.monitor.admin.entity.JobInstance;
import com.alatka.batch.monitor.admin.model.JobExecutionPageReq;
import com.alatka.batch.monitor.admin.model.JobExecutionRes;
import com.alatka.batch.monitor.admin.repository.JobExecutionRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobExecutionService {

    private JobExecutionRepository jobExecutionRepository;

    public Page<JobExecutionRes> queryPage(JobExecutionPageReq pageReq) {
        return this.jobExecutionRepository.findAll(this.condition(pageReq), pageReq.build())
                .map(entity -> {
                    JobExecutionRes res = new JobExecutionRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public List<String> statusList() {
        return Arrays.stream(BatchStatus.values()).map(Enum::name).collect(Collectors.toList());
    }

    private Specification<JobExecution> condition(JobExecutionPageReq condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (condition.getJobName() != null) {
                Root<JobInstance> jobInstanceRoot = query.from(JobInstance.class);
                list.add(criteriaBuilder.equal(root.get("jobInstanceId"), jobInstanceRoot.get("jobInstanceId")));
                list.add(criteriaBuilder.equal(jobInstanceRoot.get("jobName").as(String.class), condition.getJobName()));
            }
            if (condition.getStatus() != null) {
                list.add(root.get("status").as(String.class).in(condition.getStatus()));
            }
            if (condition.getJobExecutionId() != null) {
                list.add(criteriaBuilder.equal(root.get("jobExecutionId").as(Long.class), condition.getJobExecutionId()));
            }
            if (condition.getExitCode() != null) {
                list.add(criteriaBuilder.equal(root.get("exitCode").as(String.class), condition.getExitCode()));
            }
            if (condition.getExitMessage() != null) {
                list.add(criteriaBuilder.like(root.get("exitMessage").as(String.class), "%" + condition.getExitMessage() + "%"));
            }
            if (condition.getCreateTimeLeft() != null && condition.getCreateTimeRight() != null) {
                list.add(criteriaBuilder.between(root.get("createTime").as(LocalDateTime.class), condition.getCreateTimeLeft(), condition.getCreateTimeRight()));
            }
            if (condition.getEndTimeLeft() != null && condition.getEndTimeRight() != null) {
                list.add(criteriaBuilder.between(root.get("endTime").as(LocalDateTime.class), condition.getEndTimeLeft(), condition.getEndTimeRight()));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setJobExecutionRepository(JobExecutionRepository jobExecutionRepository) {
        this.jobExecutionRepository = jobExecutionRepository;
    }
}
