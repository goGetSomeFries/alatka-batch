package com.alatka.batch.param.builder;

import com.alatka.batch.param.entity.BatchParam;
import com.alatka.batch.param.repository.ParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BatchParamBuilder {

    private ParamRepository paramRepository;

    public String build(String jobName, String groupKey) {
        Specification<BatchParam> specA = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("enabled").as(Boolean.class), true);

        Specification<BatchParam> specB = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("name").as(String.class), jobName),
                        criteriaBuilder.equal(root.get("type").as(String.class), BatchParam.Type.job));

        Specification<BatchParam> specC = groupKey == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("name").as(String.class), groupKey),
                        criteriaBuilder.equal(root.get("type").as(String.class), BatchParam.Type.global));

        Specification<BatchParam> finalSpec = specA.and(specB.or(specC));

        return paramRepository.findAll(finalSpec)
                .stream()
                .map(entity -> entity.getKey().concat("=").concat(this.doBuild(entity.getValue())))
                .collect(Collectors.joining(","));
    }

    private String doBuild(String value) {
        return value;
    }

    @Autowired
    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }
}
