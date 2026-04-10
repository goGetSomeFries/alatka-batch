package com.alatka.batch.flow.model;

import com.alatka.batch.infra.util.JsonUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RootModel {

    private boolean enabled = true;

    private String name;

    private String desc;

    private List<ComponentModel> steps;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ComponentModel> getSteps() {
        return steps;
    }

    public void setOriginSteps(List<ComponentModel> steps) {
        this.steps = steps;
    }

    public void setSteps(List<Map<String, Object>> steps) {
        this.steps = steps.stream().map(map -> {
            ComponentModel model = JsonUtil.convertToObject(map, ComponentModel.class);
            return JsonUtil.convertToObject(map, model.getType().getClazz());
        }).collect(Collectors.toList());
    }
}
