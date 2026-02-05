package com.alatka.batch.flow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "前端页面跳转")
@Controller("batchFlowHtmlController")
@RequestMapping("/flow")
public class HtmlController {

    @Operation(summary = "流程组")
    @GetMapping("/group")
    public String group() {
        return "group";
    }

    @Operation(summary = "流程设计")
    @GetMapping("/design")
    public String design() {
        return "design";
    }

}
