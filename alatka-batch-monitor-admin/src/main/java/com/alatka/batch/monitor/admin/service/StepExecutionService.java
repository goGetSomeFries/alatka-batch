package com.alatka.batch.monitor.admin.service;

import com.alatka.batch.monitor.admin.entity.StepExecution;
import com.alatka.batch.monitor.admin.model.StepExecutionListReq;
import com.alatka.batch.monitor.admin.model.StepExecutionListRes;
import com.alatka.batch.monitor.admin.model.StepExecutionPageReq;
import com.alatka.batch.monitor.admin.model.StepExecutionRes;
import com.alatka.batch.monitor.admin.repository.StepExecutionRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StepExecutionService {

    private StepExecutionRepository stepExecutionRepository;

    public Page<StepExecutionRes> queryPage(StepExecutionPageReq pageReq) {
        return this.stepExecutionRepository.findAll(this.condition(pageReq), pageReq.build())
                .map(entity -> {
                    StepExecutionRes res = new StepExecutionRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public StepExecutionListRes queryStepExecutionList(StepExecutionListReq req) {
        StepExecutionPageReq pageReq = new StepExecutionPageReq();
        pageReq.setJobExecutionId(req.getJobExecutionId());
        pageReq.setPageNo(req.getIndex());
        pageReq.setPageSize(5);
        pageReq.setOrderBy("stepExecutionId");
        pageReq.setDirection("desc");

        Page<StepExecutionRes> page = this.queryPage(pageReq);
        StepExecutionListRes res = new StepExecutionListRes();
        res.setList(page.getContent());
        res.setHasNext(page.hasNext());
        return res;
    }

    private Specification<StepExecution> condition(StepExecutionPageReq condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            list.add(criteriaBuilder.equal(root.get("jobExecutionId").as(Long.class), condition.getJobExecutionId()));
            if (condition.getStepName() != null) {
                list.add(criteriaBuilder.like(root.get("stepName").as(String.class), "%" + condition.getStepName() + "%"));
            }
            if (condition.getStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("status").as(String.class), condition.getStatus()));
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
    public void setStepExecutionRepository(StepExecutionRepository stepExecutionRepository) {
        this.stepExecutionRepository = stepExecutionRepository;
    }
}
