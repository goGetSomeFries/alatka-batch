package com.alatka.batch.flow.service;


import com.alatka.batch.flow.AutoConfiguration;
import com.alatka.batch.flow.entity.BatchFlow;
import com.alatka.batch.flow.model.FlowDeployReq;
import com.alatka.batch.flow.model.FlowPageReq;
import com.alatka.batch.flow.model.FlowReq;
import com.alatka.batch.flow.model.FlowRes;
import com.alatka.batch.flow.repository.FlowRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlowService {

    private RestTemplate restTemplate;

    private TaskExecutor taskExecutor;

    private FlowRepository flowRepository;

    private FlowGraphService flowGraphService;

    public Long create(FlowReq req) {
        BatchFlow entity = new BatchFlow();
        BeanUtils.copyProperties(req, entity);

        BatchFlow condition = new BatchFlow();
        condition.setGroupKey(entity.getGroupKey());
        condition.setKey(entity.getKey());
        boolean exists = flowRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already for group <" + condition.getGroupKey() + ">");
        }

        return flowRepository.save(entity).getId();
    }

    public void update(FlowReq req) {
        BatchFlow entity = new BatchFlow();
        BeanUtils.copyProperties(req, entity);

        BatchFlow theOne = flowRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("id : <" + entity.getId() + "> not found"));
        if (!theOne.getKey().equals(entity.getKey())) {
            throw new IllegalArgumentException("key : <" + theOne.getKey() + "> can not be modified");
        }
        flowRepository.save(entity);
    }

    public void delete(Long id) {
        BatchFlow entity = flowRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        if (!entity.getEnabled()) {
            throw new IllegalArgumentException("id: <" + id + "> is disabled already");
        }
        entity.setEnabled(false);
    }

    public void deploy(FlowDeployReq req) {
        flowGraphService.deploy(req.getFlowIdList());
//        this.build(req);
    }

    private void build(FlowDeployReq req) {
        Map<String, String> resultMap = new HashMap<>(req.getUris().size());
        CompletableFuture<Void> completableFuture = req.getUris().stream()
                .map(uri -> uri.startsWith("http://") || uri.startsWith("https://") ? uri : "http://" + uri)
                .map(uri -> uri.concat(req.getPath()))
                .map(url -> this.doBuild(url, req.getFlowIdList(), resultMap))
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> CompletableFuture.allOf(list.toArray(new CompletableFuture[0]))));
        completableFuture.join();
    }

    private CompletableFuture<Void> doBuild(String url, List<Long> flowIds, Map<String, String> resultMap) {
        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(url, flowIds, String.class), taskExecutor)
                .handleAsync((result, ex) -> "ok".equalsIgnoreCase(result) ? "success" : "fail")
                .thenAccept(result -> resultMap.put(url, result));
    }

    public List<FlowRes> queryUndeploy() {
        List<Long> flowIds = flowGraphService.queryUndeploy();
        return flowRepository.findAllById(flowIds).stream()
                .filter(BatchFlow::getEnabled)
                .map(entity -> {
                    FlowRes res = new FlowRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                }).collect(Collectors.toList());
    }

    public Page<FlowRes> queryPage(FlowPageReq pageReq) {
        BatchFlow condition = new BatchFlow();
        BeanUtils.copyProperties(pageReq, condition);

        return flowRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    FlowRes res = new FlowRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    private Specification<BatchFlow> condition(BatchFlow condition) {
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
            if (condition.getGroupKey() != null) {
                list.add(criteriaBuilder.equal(root.get("groupKey").as(String.class), condition.getGroupKey()));
            }
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setFlowRepository(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    @Autowired
    public void setFlowGraphService(FlowGraphService flowGraphService) {
        this.flowGraphService = flowGraphService;
    }

    @Autowired
    @Qualifier(AutoConfiguration.REST_TEMPLATE_NAME)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    @Qualifier(AutoConfiguration.TASK_EXECUTOR_NAME)
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
