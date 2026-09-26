package com.alatka.batch.monitor.admin.controller;

import com.alatka.batch.infra.model.PageResMessage;
import com.alatka.batch.infra.model.ResMessage;
import com.alatka.batch.monitor.admin.model.*;
import com.alatka.batch.monitor.admin.service.StepExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "Spring Batch Step")
@RestController
@RequestMapping("/batch/monitor/step")
public class StepController {

    private StepExecutionService stepExecutionService;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @InitBinder({"stepExecutionPageReq"})
    public void initOrderBinder(WebDataBinder binder) {
        binder.setDisallowedFields("createTimeLeft");
        binder.setDisallowedFields("createTimeRight");
        binder.setDisallowedFields("endTimeLeft");
        binder.setDisallowedFields("endTimeRight");
    }

    @Operation(summary = "分页查询 Step Execution")
    @GetMapping("/page")
    public PageResMessage<StepExecutionRes> queryExecutionPage(@Valid @ParameterObject StepExecutionPageReq pageReqMessage) {
        if (pageReqMessage.getCreateTimeRange() != null) {
            String[] createTimeRange = pageReqMessage.getCreateTimeRange().split("~");
            pageReqMessage.setCreateTimeLeft(LocalDateTime.parse(createTimeRange[0], dateTimeFormatter));
            pageReqMessage.setCreateTimeRight(LocalDateTime.parse(createTimeRange[1], dateTimeFormatter));
        }
        if (pageReqMessage.getEndTimeRange() != null) {
            String[] endTimeRange = pageReqMessage.getEndTimeRange().split("~");
            pageReqMessage.setEndTimeLeft(LocalDateTime.parse(endTimeRange[0], dateTimeFormatter));
            pageReqMessage.setEndTimeRight(LocalDateTime.parse(endTimeRange[1], dateTimeFormatter));
        }
        return PageResMessage.success(stepExecutionService.queryPage(pageReqMessage));
    }

    @Operation(summary = "查询 Step Execution")
    @GetMapping("/list")
    public ResMessage<StepExecutionListRes> queryStepExecutionList(@Valid @ParameterObject StepExecutionListReq reqMessage) {
        return ResMessage.success(stepExecutionService.queryStepExecutionList(reqMessage));
    }

    @Operation(summary = "查询 Sub Job Execution")
    @Parameter(name = "jobExecutionId", description = "jobExecutionId", required = true)
    @Parameter(name = "stepExecutionId", description = "stepExecutionId", required = true)
    @GetMapping("/subJobExecution")
    public ResMessage<SubJobExecutionRes> getSubJobExecution(@RequestParam Long jobExecutionId, @RequestParam Long stepExecutionId) {
        return ResMessage.success(stepExecutionService.getSubJobExecution(jobExecutionId, stepExecutionId));
    }

    @Autowired
    public void setStepExecutionService(StepExecutionService stepExecutionService) {
        this.stepExecutionService = stepExecutionService;
    }
}
