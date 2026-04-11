package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.FlowModel;
import com.alatka.batch.flow.model.SplitModel;
import com.alatka.batch.flow.support.GraphContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SplitModelParser extends ComponentModelParser<SplitModel> {

    @Override
    protected void doParse(GraphContext context, SplitModel componentModel) {
        JsonNode currentVertex = context.getCurrentNode();
        componentModel.setTaskExecutor(currentVertex.get("Object").get("SplitNodeData").get("taskExecutor").asText());
        List<ComponentModel> list = new ArrayList<>();
        context.nextEdges().forEach(edge -> {
            context.nextVertex();
            FlowModelParser parser = Type.FLOW.getParser();
            parser.parse(context, list);
        });
        componentModel.setOriginFlows(list.stream().map(FlowModel.class::cast).collect(Collectors.toList()));
        context.nextVertex();
    }

    @Override
    protected Class<SplitModel> modelClass() {
        return SplitModel.class;
    }
}
