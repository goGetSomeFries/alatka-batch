package com.alatka.batch.monitor.admin.service;

import com.alatka.batch.infra.model.ResMessage;
import com.alatka.batch.monitor.admin.AdminAutoConfiguration;
import com.alatka.batch.monitor.admin.entity.JobExecution;
import com.alatka.batch.monitor.admin.entity.JobInstance;
import com.alatka.batch.monitor.admin.model.JobActionReq;
import com.alatka.batch.monitor.admin.model.JobExecutionPageReq;
import com.alatka.batch.monitor.admin.model.JobExecutionRes;
import com.alatka.batch.monitor.admin.repository.JobExecutionRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobExecutionService {

    private JobExecutionRepository jobExecutionRepository;

    private RestTemplate restTemplate;

    public Page<JobExecutionRes> queryPage(JobExecutionPageReq pageReq) {
        return this.jobExecutionRepository.findAll(this.condition(pageReq), pageReq.build())
                .map(entity -> {
                    JobExecutionRes res = new JobExecutionRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public void action(JobActionReq req) {
        String urlTemplate = req.getEndpoint().startsWith("http://") || req.getEndpoint().startsWith("https://") ?
                req.getEndpoint() : "http://" + req.getEndpoint();
        if (urlTemplate.endsWith("/")) {
            urlTemplate = urlTemplate.substring(0, urlTemplate.length() - 1);
        }
        urlTemplate = urlTemplate + "{path}/{actionCode}/{jobExecutionId}";
        ResMessage resMessage = restTemplate.postForObject(urlTemplate, null, ResMessage.class, "/batch/monitor/job", req.getActionCode(), req.getJobExecutionId());
        if (!resMessage.isSuccess()) {
            throw new RuntimeException(resMessage.getMsg());
        }
    }

    public List<String> statusList() {
        return Arrays.stream(BatchStatus.values()).map(Enum::name).collect(Collectors.toList());
    }

    public List<String> exitCodeList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(ExitStatus.COMPLETED.getExitCode());
        list.add(ExitStatus.EXECUTING.getExitCode());
        list.add(ExitStatus.STOPPED.getExitCode());
        list.add(ExitStatus.FAILED.getExitCode());
        list.add(ExitStatus.NOOP.getExitCode());
        list.add(ExitStatus.UNKNOWN.getExitCode());
        return list;
    }

    private Specification<JobExecution> condition(JobExecutionPageReq condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (condition.getJobName() != null) {
                Root<JobInstance> jobInstanceRoot = query.from(JobInstance.class);
                list.add(criteriaBuilder.equal(root.get("jobInstanceId"), jobInstanceRoot.get("jobInstanceId")));
                list.add(criteriaBuilder.equal(jobInstanceRoot.get("jobName").as(String.class), condition.getJobName()));
            }
            if (condition.getJobExecutionId() != null) {
                list.add(criteriaBuilder.equal(root.get("jobExecutionId").as(Long.class), condition.getJobExecutionId()));
            }
            if (condition.getStatus() != null) {
                list.add(root.get("status").as(String.class).in(condition.getStatus()));
            }
            if (condition.getExitCode() != null) {
                list.add(root.get("exitCode").as(String.class).in(condition.getExitCode()));
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

    @Autowired
    @Qualifier(AdminAutoConfiguration.REST_TEMPLATE_NAME)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
