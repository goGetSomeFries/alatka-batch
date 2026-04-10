package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.FlowModel;
import com.fasterxml.jackson.databind.JsonNode;

public class FlowModelParser extends ComponentModelParser<FlowModel> {

    @Override
    protected void doParse(GraphContext context, FlowModel componentModel) {
        JsonNode currentVertex = context.getCurrentNode();
        String beanName = currentVertex.get("Object").get("BaseData").get("beanName").asText();
        componentModel.setName(beanName);
    }

    @Override
    protected Class<FlowModel> modelClass() {
        return FlowModel.class;
    }

}
