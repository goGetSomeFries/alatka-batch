package com.alatka.batch.param.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = "前端页面跳转")
@Controller("batchParamHtmlController")
@RequestMapping("/batch")
public class HtmlController {

    @Operation(summary = "流程")
    @GetMapping("/param")
    public String param() {
        return "param";
    }

    @Operation(summary = "判断是否存在")
    @RequestMapping(value = "/param/exist", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exist() {
        return ResponseEntity.ok().build();
    }
}
