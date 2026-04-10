package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.DecisionModel;

import java.util.List;

public abstract class ExitNodeParser extends AbstractModelParser<DecisionModel.InnerModel.Exit, List<ComponentModel>> {

    @Override
    protected DecisionModel.InnerModel.Exit parse(GraphContext context, List<ComponentModel> property) {
        return this.exit();
    }

    protected abstract DecisionModel.InnerModel.Exit exit();
}
