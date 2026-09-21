package com.alatka.batch.monitor.admin.controller;

import com.alatka.batch.infra.model.PageResMessage;
import com.alatka.batch.infra.model.ResMessage;
import com.alatka.batch.monitor.admin.model.JobExecutionPageReq;
import com.alatka.batch.monitor.admin.model.JobExecutionParamsRes;
import com.alatka.batch.monitor.admin.model.JobExecutionRes;
import com.alatka.batch.monitor.admin.service.JobExecutionParamsService;
import com.alatka.batch.monitor.admin.service.JobExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "Spring Batch Job")
@RestController
@RequestMapping("/batch/monitor/job")
public class JobController {

    private JobExecutionService jobExecutionService;

    private JobExecutionParamsService jobExecutionParamsService;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @InitBinder({"jobExecutionPageReq"})
    public void initOrderBinder(WebDataBinder binder) {
        binder.setDisallowedFields("createTimeLeft");
        binder.setDisallowedFields("createTimeRight");
        binder.setDisallowedFields("endTimeLeft");
        binder.setDisallowedFields("endTimeRight");
    }

    @Operation(summary = "分页查询 Job Execution")
    @GetMapping("/page")
    public PageResMessage<JobExecutionRes> queryExecutionPage(@Valid @ParameterObject JobExecutionPageReq pageReqMessage) {
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
        return PageResMessage.success(jobExecutionService.queryPage(pageReqMessage));
    }

    @Operation(summary = "查询 Job Execution Params")
    @GetMapping("/params/list")
    public ResMessage<List<JobExecutionParamsRes>> queryExecutionParams(@RequestParam Long jobExecutionId) {
        return ResMessage.success(jobExecutionParamsService.queryList(jobExecutionId));
    }

    @Operation(summary = "查询 status")
    @GetMapping("/status/list")
    public ResMessage<List<String>> statusList() {
        return ResMessage.success(jobExecutionService.statusList());
    }

    @Autowired
    public void setJobExecutionParamsService(JobExecutionParamsService jobExecutionParamsService) {
        this.jobExecutionParamsService = jobExecutionParamsService;
    }

    @Autowired
    public void setJobExecutionService(JobExecutionService jobExecutionService) {
        this.jobExecutionService = jobExecutionService;
    }
}
