package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.RootModel;

import java.util.Map;

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

    static void execute(GraphContext context, Map<String, Object> properties) {
        StartNodeModelParser parser = Type.START.getParser();
        RootModel rootModel = parser.parse(context, properties);
        AbstractModelParser.doExecute(context, rootModel.getSteps());
    }

}
