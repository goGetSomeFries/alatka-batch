package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.flow.support.GraphContext;

import java.util.ArrayList;
import java.util.Map;

public class StartNodeModelParser extends AbstractModelParser<RootModel, Map<String, Object>> {

    @Override
    public RootModel parse(GraphContext context, Map<String, Object> properties) {
        RootModel rootModel = new RootModel();
        rootModel.setName(properties.get("key").toString());
        rootModel.setDesc(properties.get("name").toString());
        rootModel.setDesc(properties.get("group").toString());
        rootModel.setEnabled(Boolean.valueOf(properties.get("enabled").toString()));
        rootModel.setSteps(new ArrayList<>());
        return rootModel;
    }
}
