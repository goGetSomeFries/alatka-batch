package com.alatka.batch.flow.service;


import com.alatka.batch.flow.entity.BatchFlow;
import com.alatka.batch.flow.entity.BatchFlowData;
import com.alatka.batch.flow.model.FlowHistory;
import com.alatka.batch.flow.model.FlowPageReq;
import com.alatka.batch.flow.model.FlowReq;
import com.alatka.batch.flow.model.FlowRes;
import com.alatka.batch.flow.repository.FlowDataRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class FlowDataService {

    private FlowDataRepository flowDataRepository;

    private static final Charset DATA_CHARSET = Charset.forName("UTF-8");

    public Long create(FlowReq req) {
        BatchFlow entity = new BatchFlow();
        BeanUtils.copyProperties(req, entity);

        BatchFlow condition = new BatchFlow();
        condition.setGroupKey(entity.getGroupKey());
        condition.setKey(entity.getKey());
        boolean exists = flowDataRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already for group <" + condition.getGroupKey() + ">");
        }

        return flowDataRepository.save(entity).getId();
    }

    public void update(FlowReq req) {
        BatchFlow entity = new BatchFlow();
        BeanUtils.copyProperties(req, entity);

        BatchFlow theOne = flowDataRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("id : <" + entity.getId() + "> not found"));
        if (!theOne.getKey().equals(entity.getKey())) {
            throw new IllegalArgumentException("key : <" + theOne.getKey() + "> can not be modified");
        }
        flowDataRepository.save(entity);
    }

    public void delete(Long previousId) {
        BatchFlow entity = flowDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        if (!entity.getEnabled()) {
            throw new IllegalArgumentException("id: <" + id + "> is disabled already");
        }
        entity.setEnabled(false);
    }

    public List<FlowHistory> queryHistory(Long previousId) {
       List<FlowHistory> result = new ArrayList<>();
       AtomicInteger time = new AtomicInteger(1);
       this.doQueryHistory(previousId, result, time);
       return result;
    }

    private void doQueryHistory(Long previousId, List<FlowHistory> result, AtomicInteger time) {
        if (previousId == null || time.getAndIncrement() >= 5) {
            return;
        }
        BatchFlowData entity = flowDataRepository.findById(previousId)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + previousId + "> not exists."));
        FlowHistory model = new FlowHistory();
        BeanUtils.copyProperties(entity, model);
        result.add(model);
        this.doQueryHistory(entity.getPreviousId(), result, time);
    }

    public String queryData(Long flowId) {
        BatchFlowData condition = new BatchFlowData();
        condition.setFlowId(flowId);
        Optional<BatchFlowData> optional = flowDataRepository.findOne(this.condition(condition));
        return optional.isPresent() ? new String(optional.get().getData(), DATA_CHARSET) : null;
    }

    private Specification<BatchFlowData> condition(BatchFlowData condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (condition.getId() != null) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), condition.getId()));
            }
            if (condition.getFlowId() != null) {
                list.add(criteriaBuilder.equal(root.get("flowId").as(Long.class), condition.getFlowId()));
            }
            if (condition.getPreviousId() != null) {
                list.add(criteriaBuilder.equal(root.get("previousId").as(Long.class), condition.getPreviousId()));
            } else {
                list.add(criteriaBuilder.isNull(root.get("previousId").as(Long.class)));
            }
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setFlowDataRepository(FlowDataRepository flowDataRepository) {
        this.flowDataRepository = flowDataRepository;
    }
}
