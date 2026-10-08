package com.alatka.batch.monitor.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Operation(summary = "判断是否存在")
    @RequestMapping(value = "/exist", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exist() {
        return ResponseEntity.ok().build();
    }
}
