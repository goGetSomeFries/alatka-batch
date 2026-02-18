package com.alatka.batch.flow.controller;

import com.alatka.batch.flow.model.FlowPageReq;
import com.alatka.batch.flow.model.FlowReq;
import com.alatka.batch.flow.model.FlowRes;
import com.alatka.batch.flow.service.FlowService;
import com.alatka.batch.infra.model.PageResMessage;
import com.alatka.batch.infra.model.ResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "流程")
@RestController
@RequestMapping("/batch/flow")
public class FlowController {

    private FlowService flowService;

    @Operation(summary = "创建流程")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody FlowReq req) {
        return ResMessage.success(flowService.create(req));
    }

    @Operation(summary = "删除流程组")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> flowService.delete(id));
    }

    @Operation(summary = "修改流程组")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody FlowReq req) {
        return ResMessage.success(() -> flowService.update(req));
    }

    @Operation(summary = "分页查询流程组")
    @GetMapping("/page")
    public PageResMessage<FlowRes> queryPage(@Valid FlowPageReq pageReqMessage) {
        return PageResMessage.success(flowService.queryPage(pageReqMessage));
    }

    @Autowired
    public void setFlowService(FlowService flowService) {
        this.flowService = flowService;
    }

}
