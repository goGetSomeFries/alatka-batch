package com.alatka.batch.flow.controller;

import com.alatka.batch.flow.model.FlowDeployReq;
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
import java.util.List;
import java.util.Map;

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

    @Operation(summary = "删除流程")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> flowService.delete(id));
    }

    @Operation(summary = "修改流程")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody FlowReq req) {
        return ResMessage.success(() -> flowService.update(req));
    }

    @Operation(summary = "分页查询流程")
    @GetMapping("/page")
    public PageResMessage<FlowRes> queryPage(@Valid FlowPageReq pageReqMessage) {
        return PageResMessage.success(flowService.queryPage(pageReqMessage));
    }

    @Operation(summary = "查询未部署流程")
    @GetMapping("/undeploy")
    public ResMessage<List<FlowRes>> queryUndeploy() {
        return PageResMessage.success(flowService.queryUndeploy());
    }

    @Operation(summary = "部署流程")
    @PostMapping("/deploy")
    public ResMessage<Map<String, String>> deploy(@Valid @RequestBody FlowDeployReq req) {
        return ResMessage.success(flowService.deploy(req));
    }

    @Autowired
    public void setFlowService(FlowService flowService) {
        this.flowService = flowService;
    }

}
