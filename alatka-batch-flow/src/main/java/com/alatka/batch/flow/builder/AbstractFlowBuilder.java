package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.component.IComponent;
import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.JsonUtil;
import com.alatka.batch.infra.util.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.util.List;

public abstract class AbstractFlowBuilder implements FlowBuilder, InitializingBean, ApplicationContextAware {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ROOT_NAME = "alatka.batch.flow";

    private ApplicationContext applicationContext;

    @Override
    public void build() {
        List<Resource> resources = this.loadResources();
        if (resources.isEmpty()) {
            this.logger.info("No resources found. Skipping build.");
            return;
        }
        resources.forEach(this::build);
    }

    @Override
    public void build(String identity) {
        Resource resource = this.loadResource(identity);
        this.build(resource);
    }

    protected abstract List<Resource> loadResources();

    protected abstract Resource loadResource(String identity);

    private void build(Resource resource) {
        RootModel rootModel = YamlUtil.loadYaml(resource, ROOT_NAME, RootModel.class);
        if (!rootModel.isEnabled()) {
            this.logger.warn("model '{}' is disabled. Skipping build...", rootModel.getName());
            return;
        }
        this.doBuild(rootModel);
    }

    private void doBuild(RootModel rootModel) {
        rootModel.getSteps().stream()
                .flatMap(map -> map.entrySet().stream().map(entry -> JsonUtil.convertToObject(entry.getValue(), entry.getKey().getClazz())))
                .forEach(model -> {
                    applicationContext.getBeansOfType(IComponent.class).values().stream()
                            .filter(component -> component.matched(model))
                            .findFirst()
                            .ifPresent(component -> {
                            });
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
