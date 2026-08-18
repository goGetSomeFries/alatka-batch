package com.alatka.batch.example.controller;

import com.alatka.batch.param.builder.BatchParamBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "参数示例接口")
@RestController
@RequestMapping("/batch/example/param")
public class ParamExampleController {

    private BatchParamBuilder batchParamBuilder;

    @Operation(summary = "构建参数")
    @GetMapping("/build")
    public String build(@RequestParam String jobName, @RequestParam String groupKey) {
        return batchParamBuilder.build(jobName, groupKey);
    }

    @Autowired
    public void setBatchParamBuilder(BatchParamBuilder batchParamBuilder) {
        this.batchParamBuilder = batchParamBuilder;
    }
}
