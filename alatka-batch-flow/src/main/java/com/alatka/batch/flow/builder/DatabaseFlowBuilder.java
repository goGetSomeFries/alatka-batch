package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.XmlUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseFlowBuilder extends AbstractFlowBuilder {

    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_SQL = "SELECT D.D_DEPLOY_DATA FROM ALK_BATCH_FLOW_GRAPH D";

    @Override
    protected List<RootModel> loadResources() {
        String sql = QUERY_SQL +
                " JOIN ALK_BATCH_FLOW F ON D.F_ID = F.F_ID AND F.F_ENABLED = 1 WHERE D.D_CURRENT = 1 AND D.D_STATUS = 'DEPLOY'";
        List<byte[]> list = this.jdbcTemplate.queryForList(sql, Collections.emptyMap(), byte[].class);

        return list.stream().filter(Objects::nonNull).map(this::test).collect(Collectors.toList());
    }

    @Override
    protected RootModel loadResource(String identity) {
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
        return this.test(bytes);
    }

    private RootModel test(byte[] bytes) {
        String content = new String(bytes, StandardCharsets.UTF_8);
        Map<String, Object> map = XmlUtil.getMap(content);
        return null;
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
