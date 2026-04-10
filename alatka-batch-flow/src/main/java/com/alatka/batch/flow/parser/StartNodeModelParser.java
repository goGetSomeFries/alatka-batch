package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.RootModel;

import java.util.ArrayList;
import java.util.Map;

public class StartNodeModelParser extends AbstractModelParser<RootModel, Map<String, Object>> {

    @Override
    public RootModel parse(GraphContext context, Map<String, Object> property) {
        RootModel rootModel = new RootModel();
        rootModel.setName(property.get("name").toString());
        rootModel.setDesc(property.get("desc").toString());
        rootModel.setEnabled(Boolean.valueOf(property.get("enabled").toString()));
        rootModel.setSteps(new ArrayList<>());
        return rootModel;
    }
}
