package com.alatka.batch.flow.model;

import jakarta.validation.constraints.NotBlank;

public abstract class BeanComponentModel extends ComponentModel {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
