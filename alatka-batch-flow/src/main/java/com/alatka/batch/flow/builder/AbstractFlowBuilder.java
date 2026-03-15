package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.YamlUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AbstractFlowBuilder implements FlowBuilder, InitializingBean {

    @Override
    public void build() {
        String locationPattern = "classpath*:META-INF/*.flow.yaml";
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(null)
                    .getResources(locationPattern);
            for (Resource resource : resources) {
                RootModel model = YamlUtil.loadYaml(resource, "alatka.batch.flow", RootModel.class);
                System.out.println(model);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.build();
    }
}
