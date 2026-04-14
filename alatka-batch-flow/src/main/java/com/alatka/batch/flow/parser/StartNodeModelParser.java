package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.flow.support.GraphContext;

import java.util.ArrayList;
import java.util.Map;

public class StartNodeModelParser extends AbstractModelParser<RootModel, Map<String, Object>> {

    @Override
    public RootModel parse(GraphContext context, Map<String, Object> properties) {
        RootModel rootModel = new RootModel();
        rootModel.setName(properties.get("name_").toString());
        rootModel.setDesc(properties.get("desc_").toString());
        rootModel.setGroup(properties.get("group_").toString());
        rootModel.setEnabled("1".equals(properties.get("enabled_").toString()));
        rootModel.setSteps(new ArrayList<>());
        return rootModel;
    }
}
