package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.flow.support.GraphContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;

/**
 * {@link RootModel}实例解析器，用于从mxGraph.js生成的xml文件构造{@link RootModel}结构
 *
 * @author whocares
 */
public interface ModelParser {

    enum Type {
        START(new StartNodeModelParser()),
        END(new EndNodeParser()),
        STOP(new StopNodeParser()),
        FAIL(new FailNodeParser()),
        STEP(new StepModelParser()),
        FLOW(new FlowModelParser()),
        SPLIT(new SplitModelParser()),
        JOIN(new JoinNodeParser()),
        DECISION(new DecisionModelParser());

        private AbstractModelParser parser;

        Type(AbstractModelParser parser) {
            this.parser = parser;
        }

        public <T extends AbstractModelParser> T getParser() {
            return (T) parser;
        }
    }

    static void execute(GraphContext context, List<? extends ComponentModel> list) {
        JsonNode nextVertex = context.nextVertex();
        Type type = Type.valueOf(nextVertex.get("style").asText());
        Arrays.stream(Type.values())
                .filter(t -> type == t)
                .map(Type::getParser)
                .map(AbstractModelParser.class::cast)
                .findFirst()
                .ifPresent(modelParser -> modelParser.parse(context, list));
        if (type == Type.END || type == Type.DECISION) {
            return;
        }
        execute(context, list);
    }
}
