package com.alatka.batch.flow.controller;

import com.alatka.batch.flow.model.FlowGroupPageReq;
import com.alatka.batch.flow.model.FlowGroupReq;
import com.alatka.batch.flow.model.FlowGroupRes;
import com.alatka.batch.flow.service.FlowGroupService;
import com.alatka.batch.infra.model.PageResMessage;
import com.alatka.batch.infra.model.ResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Tag(name = "流程组")
@RestController
@RequestMapping("/batch/flow/group")
public class FlowGroupController {

    private FlowGroupService flowGroupService;

    @Operation(summary = "创建流程组")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody FlowGroupReq req) {
        return ResMessage.success(flowGroupService.create(req));
    }

    @Operation(summary = "删除流程组")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> flowGroupService.delete(id));
    }

    @Operation(summary = "修改流程组")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody FlowGroupReq req) {
        return ResMessage.success(() -> flowGroupService.update(req));
    }

    @Operation(summary = "分页查询流程组")
    @GetMapping("/page")
    public PageResMessage<FlowGroupRes> queryPage(@Valid FlowGroupPageReq pageReqMessage) {
        return PageResMessage.success(flowGroupService.queryPage(pageReqMessage));
    }

    @Operation(summary = "规则组kv")
    @GetMapping("/map")
    public ResMessage<Map<String, String>> getMap() {
        return ResMessage.success(flowGroupService.getMap());
    }

    @Autowired
    public void setFlowGroupService(FlowGroupService flowGroupService) {
        this.flowGroupService = flowGroupService;
    }

}
