package com.alatka.batch.param.builder;

import com.alatka.batch.param.entity.BatchParam;
import com.alatka.batch.param.repository.ParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BatchParamBuilder {

    private ParamRepository paramRepository;

    public String build(String jobName, String groupKey) {
        return this.getList(jobName, groupKey)
                .stream()
                .map(entity -> entity.getKey().concat("=").concat(this.doBuild(entity.getValue())))
                .collect(Collectors.joining(","));
    }

    public List<BatchParam> getList(String jobName, String groupKey) {
        Specification<BatchParam> specA = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("enabled").as(Boolean.class), true);

        Specification<BatchParam> specB = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("name").as(String.class), jobName),
                        criteriaBuilder.equal(root.get("type").as(String.class), BatchParam.Type.job.name()));

        Specification<BatchParam> specC = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("name").as(String.class), groupKey),
                        criteriaBuilder.equal(root.get("type").as(String.class), BatchParam.Type.global.name()));

        Specification<BatchParam> finalSpec = specA.and(specB.or(specC));

        return paramRepository.findAll(finalSpec);
    }

    private String doBuild(String value) {
        return value;
    }

    @Autowired
    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }
}
