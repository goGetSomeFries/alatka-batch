package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.FlowModel;
import com.alatka.batch.flow.model.SplitModel;
import com.alatka.batch.flow.support.GraphContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class SplitModelParser extends ComponentModelParser<SplitModel> {

    @Override
    protected void doParse(GraphContext context, SplitModel componentModel) {
        JsonNode currentVertex = context.getCurrentNode();
        // TODO
        componentModel.setTaskExecutor(currentVertex.get("Object").get("").get("taskExecutor").asText());
        List<FlowModel> list = new ArrayList<>();
        context.nextEdges().forEach(edge -> ModelParser.execute(context, list));
        componentModel.setFlows(list);
        context.nextVertex();
    }

    @Override
    protected Class<SplitModel> modelClass() {
        return SplitModel.class;
    }
}
