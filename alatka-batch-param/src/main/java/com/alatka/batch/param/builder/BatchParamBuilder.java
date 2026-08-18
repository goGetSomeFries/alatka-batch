package com.alatka.batch.param.builder;

import com.alatka.batch.infra.util.ApplicationContextUtil;
import com.alatka.batch.param.entity.BatchParam;
import com.alatka.batch.param.property.FallbackPropertyConverter;
import com.alatka.batch.param.property.PropertyConverter;
import com.alatka.batch.param.repository.ParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
                .peek(entity -> entity.setValue(this.formatValue(entity.getValue())))
                .map(entity -> entity.getKey().concat("=").concat(entity.getValue()))
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
        Sort sort = Sort.by("key");

        return paramRepository.findAll(finalSpec, sort);
    }

    private String formatValue(String value) {
        PropertyConverter propertyConverter = ApplicationContextUtil.getBeansOfType(PropertyConverter.class).values()
                .stream()
                .filter(bean -> bean.matched(value))
                .findFirst()
                .orElseGet(() -> ApplicationContextUtil.getBean(FallbackPropertyConverter.class));
        return propertyConverter.convert(value);
    }

    @Autowired
    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }
}
