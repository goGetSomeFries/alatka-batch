package com.alatka.batch.flow.model;

import com.alatka.batch.infra.util.JsonUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RootModel {

    private boolean enabled = true;

    @NotBlank
    private String name;

    @NotBlank
    private String desc;

    private String group;

    private List<String> listeners;

    @NotEmpty
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getListeners() {
        return listeners;
    }

    public void setListeners(List<String> listeners) {
        this.listeners = listeners;
    }

    public List<ComponentModel> getSteps() {
        return steps;
    }

    /**
     * 用于Jackson框架映射
     */
    public void setSteps(List<Map<String, Object>> steps) {
        this.steps = steps.stream().map(map -> {
            ComponentModel model = JsonUtil.convertToObject(map, ComponentModel.class);
            return JsonUtil.convertToObject(map, model.getType().getClazz());
        }).collect(Collectors.toList());
    }
}
