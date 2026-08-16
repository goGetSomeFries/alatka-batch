package com.alatka.batch.param.admin.controller;

import com.alatka.batch.infra.model.PageResMessage;
import com.alatka.batch.infra.model.ResMessage;
import com.alatka.batch.param.admin.model.ParamPageReq;
import com.alatka.batch.param.admin.model.ParamReq;
import com.alatka.batch.param.admin.model.ParamRes;
import com.alatka.batch.param.admin.service.ParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "参数")
@RestController
@RequestMapping("/batch/param")
public class ParamController {

    private ParamService paramService;

    @Operation(summary = "创建")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody ParamReq req) {
        return ResMessage.success(paramService.create(req));
    }

    @Operation(summary = "删除")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> paramService.delete(id));
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody ParamReq req) {
        return ResMessage.success(() -> paramService.update(req));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public PageResMessage<ParamRes> queryPage(@Valid @ParameterObject ParamPageReq pageReqMessage) {
        return PageResMessage.success(paramService.queryPage(pageReqMessage));
    }

    @Operation(summary = "查询列表")
    @GetMapping("/list")
    public ResMessage<List<ParamRes>> queryList(@RequestParam String jobName, @RequestParam String groupKey) {
        return ResMessage.success(paramService.getList(jobName, groupKey));
    }

    @Autowired
    public void setParamService(ParamService paramService) {
        this.paramService = paramService;
    }

}
