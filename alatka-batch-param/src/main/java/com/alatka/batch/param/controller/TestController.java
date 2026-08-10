package com.alatka.batch.param.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "接口")
@RestController
@RequestMapping("/batch/")
public class TestController {

    private JobOperator jobOperator;

    @Operation(summary = "执行job")
    @PostMapping("/start/{jobName}")
    public void start(@PathVariable String jobName, @RequestBody Map<String, String> params) {
        String parameters = params.entrySet().stream().map(entry -> entry.getKey().concat("=").concat(entry.getValue()))
                .collect(Collectors.joining(","));
        try {
            jobOperator.start(jobName, parameters);
        } catch (NoSuchJobException | JobInstanceAlreadyExistsException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }
}
