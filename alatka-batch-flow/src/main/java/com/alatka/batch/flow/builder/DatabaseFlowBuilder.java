package com.alatka.batch.flow.builder;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseFlowBuilder extends AbstractFlowBuilder {

    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_SQL = "SELECT FROM ALK_BATCH_FLOW_GRAPH";

    @Override
    protected List<Resource> loadResources() {
        return null;
    }

    @Override
    protected Resource loadResource(String identity) {
        if (!StringUtils.hasLength(identity)) {
            throw new IllegalArgumentException("identity must not be empty");
        }
        Long id = Long.valueOf(identity);
        String sql = "SELECT FROM ALK_BATCH_FLOW_GRAPH WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        byte[] bytes = this.jdbcTemplate.queryForObject(sql, params, byte[].class);
        if (bytes == null) {
            throw new IllegalArgumentException("No data found for id " + id);
        }
        return new ByteArrayResource(bytes);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        super.afterPropertiesSet();
    }
}
