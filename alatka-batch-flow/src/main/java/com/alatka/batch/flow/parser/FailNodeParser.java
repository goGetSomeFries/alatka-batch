package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.DecisionModel;

public class FailNodeParser extends ExitNodeParser {

    @Override
    protected DecisionModel.InnerModel.Exit exit() {
        return DecisionModel.InnerModel.Exit.failed;
    }
}
