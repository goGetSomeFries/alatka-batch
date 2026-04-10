package com.alatka.batch.flow.parser;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.support.GraphContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;

import java.util.List;

public abstract class ComponentModelParser<T extends ComponentModel> extends AbstractModelParser<T, List<ComponentModel>> {

    @Override
    protected T parse(GraphContext context, List<ComponentModel> list) {
        JsonNode currentVertex = context.getCurrentNode();
        T componentModel = BeanUtils.instantiateClass(this.modelClass());
        componentModel.setType(ComponentModel.Type.valueOf(currentVertex.get("type").asText()));
        list.add(componentModel);
        this.doParse(context, componentModel);
        return componentModel;
    }

    protected abstract void doParse(GraphContext context, T componentModel);

    protected abstract Class<T> modelClass();
}
