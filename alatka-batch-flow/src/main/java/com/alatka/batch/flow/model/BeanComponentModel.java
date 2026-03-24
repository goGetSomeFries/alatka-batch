package com.alatka.batch.flow.model;

public abstract class BeanComponentModel extends ComponentModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
