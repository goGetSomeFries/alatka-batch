package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.util.List;

public abstract class AbstractFlowBuilder implements FlowBuilder, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ROOT_NAME = "alatka.batch.flow";

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

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.build();
    }
}
