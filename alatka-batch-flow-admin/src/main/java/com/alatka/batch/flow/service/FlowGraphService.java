package com.alatka.batch.flow.service;


import com.alatka.batch.flow.entity.BatchFlowGraph;
import com.alatka.batch.flow.model.FlowGraphHistory;
import com.alatka.batch.flow.model.FlowGraphReq;
import com.alatka.batch.flow.repository.FlowGraphRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class FlowGraphService {

    private FlowGraphRepository flowGraphRepository;

    private static final Charset DATA_CHARSET = Charset.forName("UTF-8");

    public void save(FlowGraphReq req) {
        BatchFlowGraph condition = new BatchFlowGraph();
        condition.setFlowId(req.getFlowId());
        condition.setStatus("SAVE");
        Optional<BatchFlowGraph> optional = flowGraphRepository.findOne(this.condition(condition));
        if (optional.isPresent()) {
            BatchFlowGraph entity = optional.get();
            entity.setData(req.getData().getBytes(DATA_CHARSET));
        } else {
            BatchFlowGraph entity = new BatchFlowGraph();
            entity.setFlowId(req.getFlowId());
            entity.setStatus("SAVE");
            entity.setData(req.getData().getBytes(DATA_CHARSET));

            condition.setStatus("DEPLOY");
            flowGraphRepository.findOne(this.condition(condition))
                    .ifPresent(e -> entity.setPreviousId(e.getId()));
            flowGraphRepository.save(entity);
        }
    }

    public void deploy(Long id) {

    }

    public void delete(Long previousId) {
        List<Long> result = new ArrayList<>();
        AtomicReference<Long> theOne = new AtomicReference(previousId);
        while (theOne.get() != null) {
            BatchFlowGraph entity = flowGraphRepository.findById(theOne.get())
                    .orElseThrow(() -> new IllegalArgumentException("id: <" + theOne.get() + "> not found"));
            result.add(entity.getId());
            theOne.set(entity.getPreviousId());
        }
        flowGraphRepository.deleteAllById(result);
    }

    public List<FlowGraphHistory> queryHistory(Long previousId) {
        List<FlowGraphHistory> result = new ArrayList<>();
        AtomicInteger time = new AtomicInteger(1);
        this.doQueryHistory(previousId, result, time);
        return result;
    }

    private void doQueryHistory(Long previousId, List<FlowGraphHistory> result, AtomicInteger time) {
        if (previousId == null || time.getAndIncrement() >= 5) {
            return;
        }
        BatchFlowGraph entity = flowGraphRepository.findById(previousId)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + previousId + "> not exists."));
        FlowGraphHistory model = new FlowGraphHistory();
        BeanUtils.copyProperties(entity, model);
        result.add(model);
        this.doQueryHistory(entity.getPreviousId(), result, time);
    }

    public String queryData(Long flowId) {
        BatchFlowGraph condition = new BatchFlowGraph();
        condition.setFlowId(flowId);
        Optional<BatchFlowGraph> optional = flowGraphRepository.findOne(this.condition(condition));
        return optional.isPresent() ? new String(optional.get().getData(), DATA_CHARSET) : null;
    }

    private Specification<BatchFlowGraph> condition(BatchFlowGraph condition) {
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
    public void setFlowGraphRepository(FlowGraphRepository flowGraphRepository) {
        this.flowGraphRepository = flowGraphRepository;
    }
}
