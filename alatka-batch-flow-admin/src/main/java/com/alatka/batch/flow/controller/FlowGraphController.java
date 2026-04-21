package com.alatka.batch.flow.controller;

import com.alatka.batch.flow.model.FlowGraphHistory;
import com.alatka.batch.flow.model.FlowGraphReq;
import com.alatka.batch.flow.service.FlowGraphService;
import com.alatka.batch.infra.model.ResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "流程图")
@RestController
@RequestMapping("/batch/flow/graph")
public class FlowGraphController {

    private FlowGraphService flowGraphService;

    @Operation(summary = "保存流程图")
    @PostMapping("/save")
    public ResMessage<Void> save(@Valid @RequestBody FlowGraphReq req) {
        return ResMessage.success(() -> flowGraphService.save(req));
    }

    @Operation(summary = "查询流程图历史")
    @Parameter(name = "flowId", description = "流程编号", required = true)
    @Parameter(name = "previousId", description = "编号")
    @GetMapping("/history")
    public ResMessage<List<FlowGraphHistory>> queryHistory(@RequestParam Long flowId,
                                                           @RequestParam(required = false) Long previousId) {
        return ResMessage.success(flowGraphService.queryHistory(flowId, previousId));
    }

    @Operation(summary = "查询流程图数据")
    @Parameter(name = "flowId", description = "流程ID", required = true)
    @Parameter(name = "id", description = "ID")
    @GetMapping("/getData")
    public ResMessage<String> getData(@RequestParam Long flowId, @RequestParam(required = false) Long id) {
        return ResMessage.success(flowGraphService.queryData(flowId, id));
    }

    @Operation(summary = "删除历史流程图")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> flowGraphService.delete(id));
    }

    @Autowired
    public void setFlowGraphService(FlowGraphService flowGraphService) {
        this.flowGraphService = flowGraphService;
    }
}
