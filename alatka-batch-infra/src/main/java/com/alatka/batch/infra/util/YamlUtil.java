package com.alatka.batch.infra.util;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public class YamlUtil {

    private static final YamlPropertySourceLoader LOADER = new YamlPropertySourceLoader();

    public static <T> T loadYaml(Resource resource, String rootName, Class<T> clazz) {
        try {
            List<PropertySource<?>> list = LOADER.load(resource.getFilename(), resource);
            ConfigurationPropertySource source = ConfigurationPropertySource.from(list.get(0));
            Binder binder = new Binder(source);
            return binder.bind(rootName, clazz).get();
        } catch (IOException e) {
            throw new RuntimeException("加载Yaml异常", e);
        }
    }

}
