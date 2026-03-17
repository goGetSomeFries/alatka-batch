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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlowGraphService {

    private FlowGraphRepository flowGraphRepository;

    private static final Charset DATA_CHARSET = StandardCharsets.UTF_8;

    public void save(FlowGraphReq req) {
        BatchFlowGraph condition = new BatchFlowGraph();
        condition.setFlowId(req.getFlowId());
        condition.setCurrent(true);
        condition.setStatus("SAVE");
        Optional<BatchFlowGraph> optional = flowGraphRepository.findOne(this.condition(condition));
        if (optional.isPresent()) {
            BatchFlowGraph entity = optional.get();
            entity.setOriginData(req.getData().getBytes(DATA_CHARSET));
        } else {
            BatchFlowGraph entity = new BatchFlowGraph();
            entity.setFlowId(req.getFlowId());
            entity.setCurrent(true);
            entity.setStatus("SAVE");
            entity.setOriginData(req.getData().getBytes(DATA_CHARSET));

            condition.setStatus("DEPLOY");
            flowGraphRepository.findOne(this.condition(condition))
                    .ifPresent(e -> {
                        e.setCurrent(false);
                        entity.setPreviousId(e.getId());
                    });
            flowGraphRepository.save(entity);
        }
    }

    public void deploy(List<Long> flowIds) {
        flowIds.forEach(flowId -> {
            BatchFlowGraph condition = new BatchFlowGraph();
            condition.setFlowId(flowId);
            condition.setCurrent(true);
            condition.setStatus("SAVE");
            BatchFlowGraph entity = flowGraphRepository.findOne(this.condition(condition))
                    .orElseThrow(() -> new IllegalArgumentException("没有未部署的流程图"));
            entity.setStatus("DEPLOY");
            // TODO
        });
    }

    public void delete(Long id) {
        BatchFlowGraph entity = flowGraphRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        if (entity.getCurrent()) {
            throw new IllegalArgumentException("不能删除最新节点！");
        }
        flowGraphRepository.delete(entity);

        flowGraphRepository.updatePreviousId(entity.getPreviousId(), id);
    }

    public List<Long> queryUndeploy() {
        BatchFlowGraph condition = new BatchFlowGraph();
        condition.setCurrent(true);
        condition.setStatus("SAVE");
        return flowGraphRepository.findAll(this.condition(condition))
                .stream().map(BatchFlowGraph::getFlowId).collect(Collectors.toList());
    }

    public List<FlowGraphHistory> queryHistory(Long flowId, Long previousId) {
        List<FlowGraphHistory> result = new ArrayList<>();
        AtomicInteger time = new AtomicInteger(1);
        if (previousId == null) {
            BatchFlowGraph condition = new BatchFlowGraph();
            condition.setFlowId(flowId);
            condition.setCurrent(true);
            previousId = flowGraphRepository.findOne(this.condition(condition))
                    .map(entity -> {
                        FlowGraphHistory model = new FlowGraphHistory();
                        BeanUtils.copyProperties(entity, model);
                        result.add(model);
                        return entity.getPreviousId();
                    }).orElse(null);
        }
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

    public String queryData(Long flowId, Long id) {
        if (id == null) {
            BatchFlowGraph condition = new BatchFlowGraph();
            condition.setFlowId(flowId);
            condition.setCurrent(true);
            Optional<BatchFlowGraph> optional = flowGraphRepository.findOne(this.condition(condition));
            return optional.map(entity -> new String(entity.getOriginData(), DATA_CHARSET)).orElse(null);
        }
        return flowGraphRepository.findById(id).map(entity -> new String(entity.getOriginData(), DATA_CHARSET))
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found."));
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
            if (condition.getStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("status").as(String.class), condition.getStatus()));
            }
            if (condition.getCurrent() != null) {
                list.add(criteriaBuilder.equal(root.get("current").as(Boolean.class), condition.getCurrent()));
            }
            if (condition.getPreviousId() != null) {
                list.add(criteriaBuilder.equal(root.get("previousId").as(Long.class), condition.getPreviousId()));
            }
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setFlowGraphRepository(FlowGraphRepository flowGraphRepository) {
        this.flowGraphRepository = flowGraphRepository;
    }

}
