package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractModelParser<T, E> implements ModelParser {

    protected abstract T parse(GraphContext context, E property);

    protected static void doExecute(GraphContext context, List<? extends ComponentModel> list) {
        JsonNode nextVertex = context.nextVertex();
        Arrays.stream(Type.values())
                .filter(type -> type == Type.valueOf(nextVertex.get("style").asText()))
                .map(Type::getParser)
                .map(AbstractModelParser.class::cast)
                .findFirst()
                .ifPresent(modelParser -> modelParser.parse(context, list));
        doExecute(context, list);
    }
}
