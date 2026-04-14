package com.alatka.batch.example.controller;

import com.alatka.batch.flow.builder.FlowBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "示例接口")
@RestController
@RequestMapping("/batch/example")
public class ExampleController {

    private FlowBuilder flowBuilder;

    private JobOperator jobOperator;

    @Operation(summary = "构建流程")
    @PostMapping("/build")
    public String build(@RequestBody List<Long> flowIds) {
        flowIds.stream().map(String::valueOf).forEach(flowBuilder::build);
        return "ok";
    }

    @Operation(summary = "执行job")
    @PostMapping("/execute/{jobName}")
    public void execute(@PathVariable String jobName, @RequestBody Map<String, String> params) {
        String parameters = params.entrySet().stream().map(entry -> entry.getKey().concat("=").concat(entry.getValue()))
                .collect(Collectors.joining(","));
        try {
            jobOperator.start(jobName, parameters);
        } catch (NoSuchJobException | JobInstanceAlreadyExistsException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setFlowBuilder(FlowBuilder flowBuilder) {
        this.flowBuilder = flowBuilder;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }
}
