package com.alatka.batch.infra.util;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class YamlUtil {

    private static YamlPropertySourceLoader LOADER = new YamlPropertySourceLoader();

    public static <T> T loadYaml(Resource resource, String rootName, Class<T> clazz) {
        try {
            List<PropertySource<?>> list = LOADER.load(rootName, resource);
            Map<String, Object> map = (Map<String, Object>) list.get(0).getSource();
            Binder binder = new Binder(new MapConfigurationPropertySource(map));
            return binder.bind(rootName, clazz).get();
        } catch (IOException e) {
            throw new RuntimeException("加载Yaml异常", e);
        }
    }

}
