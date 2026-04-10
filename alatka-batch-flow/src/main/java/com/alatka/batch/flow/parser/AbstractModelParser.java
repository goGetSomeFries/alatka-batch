package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.support.GraphContext;

public abstract class AbstractModelParser<T, E> implements ModelParser {

    protected abstract T parse(GraphContext context, E property);

}
