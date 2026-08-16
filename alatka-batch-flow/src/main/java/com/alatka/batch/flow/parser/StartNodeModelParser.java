package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.flow.support.GraphContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class StartNodeModelParser extends AbstractModelParser<RootModel, Map<String, Object>> {

    @Override
    public RootModel parse(GraphContext context, Map<String, Object> properties) {
        RootModel rootModel = new RootModel();
        rootModel.setName(properties.get("name_").toString());
        rootModel.setDesc(properties.get("desc_").toString());
        rootModel.setListeners(properties.get("listeners_") == null ? null :
                Arrays.stream(properties.get("listeners_").toString().split(",")).map(String::trim).collect(Collectors.toList()));
        rootModel.setGroup(properties.get("group_").toString());
        rootModel.setEnabled("1".equals(properties.get("enabled_").toString()));
        rootModel.setSteps(new ArrayList<>());
        return rootModel;
    }
}
