package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.DecisionModel;
import com.alatka.batch.flow.support.GraphContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionModelParser extends ComponentModelParser<DecisionModel> {

    @Override
    protected void doParse(GraphContext context, DecisionModel componentModel) {
        JsonNode currentVertex = context.getCurrentNode();
        String beanName = currentVertex.get("Object").get("BaseNodeData").get("beanName").asText();
        componentModel.setName(beanName);

        List<DecisionModel.InnerModel> decisions = context.nextEdges().map(edge -> {
            DecisionModel.InnerModel innerModel = new DecisionModel.InnerModel();
            innerModel.setWhen(edge.get("value").asText());
            JsonNode nextVertex = context.nextVertex();
            Type type = Type.valueOf(nextVertex.get("style").asText());
            if (type == Type.END || type == Type.FAIL || type == Type.STOP) {
                ExitNodeParser parser = type.getParser();
                innerModel.setExit(parser.parse(context, null));
            } else {
                context.resetCurrentNode(edge);
                List<ComponentModel> list = new ArrayList<>();
                ModelParser.execute(context, list);
                innerModel.setOriginTo(list);
            }
            return innerModel;
        }).collect(Collectors.toList());
        componentModel.setOriginDecisions(decisions);
    }

    @Override
    protected Class<DecisionModel> modelClass() {
        return DecisionModel.class;
    }
}
