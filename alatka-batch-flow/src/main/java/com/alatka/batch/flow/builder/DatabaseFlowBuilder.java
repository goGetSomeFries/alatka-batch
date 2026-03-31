package com.alatka.batch.flow.builder;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseFlowBuilder extends AbstractFlowBuilder {

    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_SQL = "SELECT D.D_DEPLOY_DATA FROM ALK_BATCH_FLOW_GRAPH D";

    @Override
    protected List<Resource> loadResources() {
        String sql = QUERY_SQL +
                " JOIN ALK_BATCH_FLOW F ON D.F_ID = F.F_ID AND F.F_ENABLED = 1 WHERE D.D_CURRENT = 1 AND D.D_STATUS = 'DEPLOY'";
        List<byte[]> list = this.jdbcTemplate.queryForList(sql, Collections.emptyMap(), byte[].class);

        return list.stream().filter(Objects::nonNull).map(ByteArrayResource::new).collect(Collectors.toList());
    }

    @Override
    protected Resource loadResource(String identity) {
        if (!StringUtils.hasLength(identity)) {
            throw new IllegalArgumentException("identity must not be empty");
        }
        Long id = Long.valueOf(identity);
        String sql = QUERY_SQL + " WHERE D.ID = :id";
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
    public void afterPropertiesSet() {
        if (dataSource == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
