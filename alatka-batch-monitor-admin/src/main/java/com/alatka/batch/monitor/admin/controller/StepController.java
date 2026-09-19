package com.alatka.batch.monitor.admin.controller;

import com.alatka.batch.infra.model.PageResMessage;
import com.alatka.batch.monitor.admin.model.StepExecutionPageReq;
import com.alatka.batch.monitor.admin.model.StepExecutionRes;
import com.alatka.batch.monitor.admin.service.StepExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Spring Batch Step")
@RestController
@RequestMapping("/batch/monitor/step")
public class StepController {

    private StepExecutionService stepExecutionService;

    @Operation(summary = "分页查询 Step Execution")
    @GetMapping("/page")
    public PageResMessage<StepExecutionRes> queryExecutionPage(@Valid @ParameterObject StepExecutionPageReq pageReqMessage) {
        return PageResMessage.success(stepExecutionService.queryPage(pageReqMessage));
    }

    @Autowired
    public void setStepExecutionService(StepExecutionService stepExecutionService) {
        this.stepExecutionService = stepExecutionService;
    }
}
