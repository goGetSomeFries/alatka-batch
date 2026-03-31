package com.alatka.batch.flow.builder;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFlowBuilder extends AbstractFlowBuilder {

    private String classpath = "classpath*:META-INF/";

    private static final String[] FILE_SUFFIX = new String[]{".flow.yml", ".flow.yaml"};

    @Override
    protected List<Resource> loadResources() {
        return Arrays.stream(FILE_SUFFIX)
                .map(suffix -> classpath + "*" + suffix)
                .map(locationPattern -> {
                    try {
                        return ResourcePatternUtils.getResourcePatternResolver(null)
                                .getResources(locationPattern);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }

    @Override
    protected Resource loadResource(String filePath) {
        if (!StringUtils.hasLength(filePath)) {
            throw new IllegalArgumentException("File path must not be empty");
        }
        return new ClassPathResource(filePath);
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    @Override
    public void afterPropertiesSet() {
        if (!StringUtils.hasLength(this.classpath)) {
            throw new IllegalArgumentException("Property 'classpath' is required");
        }
    }
}
