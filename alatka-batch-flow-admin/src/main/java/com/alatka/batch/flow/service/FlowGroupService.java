package com.alatka.batch.flow.service;


import com.alatka.batch.flow.entity.BatchFlowGroup;
import com.alatka.batch.flow.model.FlowGroupPageReq;
import com.alatka.batch.flow.model.FlowGroupReq;
import com.alatka.batch.flow.model.FlowGroupRes;
import com.alatka.batch.flow.repository.FlowGroupRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlowGroupService {

    private FlowGroupRepository flowGroupRepository;

    public Long create(FlowGroupReq req) {
        BatchFlowGroup entity = new BatchFlowGroup();
        BeanUtils.copyProperties(req, entity);

        BatchFlowGroup condition = new BatchFlowGroup();
        condition.setKey(entity.getKey());
        boolean exists = flowGroupRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already");
        }

        return flowGroupRepository.save(entity).getId();
    }

    public void update(FlowGroupReq req) {
        BatchFlowGroup entity = new BatchFlowGroup();
        BeanUtils.copyProperties(req, entity);

        BatchFlowGroup theOne = flowGroupRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("id : <" + entity.getId() + "> not found"));
        if (!theOne.getKey().equals(entity.getKey())) {
            throw new IllegalArgumentException("key : <" + theOne.getKey() + "> can not be modified");
        }
        flowGroupRepository.save(entity);
    }

    public void delete(Long id) {
        BatchFlowGroup entity = flowGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        if (!entity.getEnabled()) {
            throw new IllegalArgumentException("id: <" + id + "> is disabled already");
        }
        entity.setEnabled(false);
    }

    public Page<FlowGroupRes> queryPage(FlowGroupPageReq pageReq) {
        BatchFlowGroup condition = new BatchFlowGroup();
        BeanUtils.copyProperties(pageReq, condition);

        return flowGroupRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    FlowGroupRes res = new FlowGroupRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public Map<String, String> getMap() {
        BatchFlowGroup condition = new BatchFlowGroup();
        condition.setEnabled(true);
        List<BatchFlowGroup> list = flowGroupRepository.findAll(this.condition(condition));
        return list.stream()
                .sorted(Comparator.comparing(BatchFlowGroup::getId))
                .collect(Collectors.toMap(BatchFlowGroup::getKey,
                        BatchFlowGroup::getName,
                        (o, n) -> n,
                        LinkedHashMap::new));
    }

    private Specification<BatchFlowGroup> condition(BatchFlowGroup condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (condition.getId() != null) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), condition.getId()));
            }
            if (condition.getKey() != null) {
                list.add(criteriaBuilder.equal(root.get("key").as(String.class), condition.getKey()));
            }
            if (condition.getName() != null) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + condition.getName() + "%"));
            }
            if (condition.getEnabled() != null) {
                list.add(criteriaBuilder.equal(root.get("enabled").as(Boolean.class), condition.getEnabled()));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setFlowGroupRepository(FlowGroupRepository flowGroupRepository) {
        this.flowGroupRepository = flowGroupRepository;
    }
}
