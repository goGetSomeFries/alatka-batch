package com.alatka.batch.monitor.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "前端页面跳转")
@Controller("batchMonitorHtmlController")
@RequestMapping("/batch/monitor")
public class HtmlController {

    @Operation(summary = "Job")
    @GetMapping("/job")
    public String job() {
        return "job";
    }

    @Operation(summary = "Step")
    @GetMapping("/step")
    public String step() {
        return "step";
    }
}
