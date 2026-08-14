package com.alatka.batch.param.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "前端页面跳转")
@Controller("batchParamHtmlController")
@RequestMapping("/batch")
public class HtmlController {

    @Operation(summary = "流程")
    @GetMapping("/param")
    public String param() {
        return "param";
    }
}
