package com.alatka.batch.param.admin.service;

import com.alatka.batch.param.admin.model.ParamPageReq;
import com.alatka.batch.param.admin.model.ParamReq;
import com.alatka.batch.param.admin.model.ParamRes;
import com.alatka.batch.param.builder.BatchParamBuilder;
import com.alatka.batch.param.entity.BatchParam;
import com.alatka.batch.param.repository.ParamRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParamService {

    private ParamRepository paramRepository;

    private BatchParamBuilder batchParamBuilder;

    public Long create(ParamReq req) {
        BatchParam entity = new BatchParam();
        BeanUtils.copyProperties(req, entity);

        BatchParam condition = new BatchParam();
        condition.setName(req.getName());
        condition.setKey(req.getKey());
        boolean exists = paramRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already for name <" + condition.getName() + ">");
        }

        return paramRepository.save(entity).getId();
    }

    public void update(ParamReq req) {
        BatchParam entity = new BatchParam();
        BeanUtils.copyProperties(req, entity);

        paramRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("id : <" + entity.getId() + "> not found"));
        paramRepository.save(entity);
    }

    public void delete(Long id) {
        BatchParam entity = paramRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        paramRepository.delete(entity);
    }

    public Page<ParamRes> queryPage(ParamPageReq pageReq) {
        BatchParam condition = new BatchParam();
        BeanUtils.copyProperties(pageReq, condition);

        return paramRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    ParamRes res = new ParamRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public List<ParamRes> getList(String jobName, String groupKey) {
        return batchParamBuilder.getList(jobName, groupKey).stream()
                .map(entity -> {
                    ParamRes res = new ParamRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                }).collect(Collectors.toList());
    }

    private Specification<BatchParam> condition(BatchParam condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (condition.getId() != null) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), condition.getId()));
            }
            if (condition.getName() != null) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + condition.getName() + "%"));
            }
            if (condition.getKey() != null) {
                list.add(criteriaBuilder.like(root.get("key").as(String.class), "%" + condition.getKey() + "%"));
            }
            if (condition.getDesc() != null) {
                list.add(criteriaBuilder.like(root.get("desc").as(String.class), "%" + condition.getDesc() + "%"));
            }
            if (condition.getType() != null) {
                list.add(criteriaBuilder.equal(root.get("type").as(String.class), condition.getType()));
            }
            if (condition.getEnabled() != null) {
                list.add(criteriaBuilder.equal(root.get("enabled").as(Boolean.class), condition.getEnabled()));
            }
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Autowired
    public void setBatchParamBuilder(BatchParamBuilder batchParamBuilder) {
        this.batchParamBuilder = batchParamBuilder;
    }
}
