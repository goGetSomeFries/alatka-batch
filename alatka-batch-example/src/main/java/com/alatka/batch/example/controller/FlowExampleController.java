package com.alatka.batch.example.controller;

import com.alatka.batch.flow.builder.BatchFlowBuilder;
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
import java.util.Properties;
import java.util.stream.Collectors;

@Tag(name = "流程示例接口")
@RestController
@RequestMapping("/batch/example/flow")
public class FlowExampleController {

    private BatchFlowBuilder batchFlowBuilder;

    private JobOperator jobOperator;

    @Operation(summary = "构建流程")
    @PostMapping("/build")
    public String build(@RequestBody List<Long> flowIds) {
        flowIds.stream().map(String::valueOf).forEach(batchFlowBuilder::build);
        return "ok";
    }

    @Operation(summary = "执行job")
    @PostMapping("/execute/{jobName}")
    public void execute(@PathVariable String jobName, @RequestBody Map<String, String> params) {
        Properties properties = new Properties();
        properties.putAll(params);
        try {
            jobOperator.start(jobName, properties);
        } catch (NoSuchJobException | JobInstanceAlreadyExistsException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setBatchFlowBuilder(BatchFlowBuilder batchFlowBuilder) {
        this.batchFlowBuilder = batchFlowBuilder;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }
}
