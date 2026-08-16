package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.StepModel;
import com.alatka.batch.flow.support.GraphContext;
import com.fasterxml.jackson.databind.JsonNode;

public class StepModelParser extends ComponentModelParser<StepModel> {

    @Override
    protected void doParse(GraphContext context, StepModel componentModel) {
        JsonNode currentVertex = context.getCurrentNode();
        String beanName = currentVertex.get("Object").get("BaseNodeData").get("beanName").asText();
        componentModel.setName(beanName);
    }

    @Override
    protected Class<StepModel> modelClass() {
        return StepModel.class;
    }

}
