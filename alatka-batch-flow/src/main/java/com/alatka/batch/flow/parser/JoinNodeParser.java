package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;

import java.util.List;

public class JoinNodeParser extends AbstractModelParser<Void, List<ComponentModel>> {

    @Override
    public Void parse(GraphContext context, List<ComponentModel> list) {
        context.nextVertex();
        return null;
    }
}
