package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.admin.model.DecisionModel;

public class EndNodeParser extends ExitNodeParser {

    @Override
    protected DecisionModel.InnerModel.Exit exit() {
        return DecisionModel.InnerModel.Exit.end;
    }
}
