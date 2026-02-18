package com.alatka.batch.flow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "前端页面跳转")
@Controller("batchFlowHtmlController")
@RequestMapping("/batch")
public class HtmlController {

    @Operation(summary = "流程组")
    @GetMapping("/flow/group")
    public String group() {
        return "group";
    }

    @Operation(summary = "流程")
    @GetMapping("/flow")
    public String flow() {
        return "flow";
    }

    @Operation(summary = "流程设计")
    @GetMapping("/flow/design")
    public String design() {
        return "design";
    }

}
