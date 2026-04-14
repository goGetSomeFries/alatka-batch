package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.flow.parser.ModelParser;
import com.alatka.batch.flow.parser.StartNodeModelParser;
import com.alatka.batch.flow.support.GraphContext;
import com.alatka.batch.infra.util.XmlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库形式解析器
 *
 * @author whocares
 */
public class DatabaseFlowBuilder extends AbstractFlowBuilder {

    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_SQL =
            "SELECT D.D_DATA AS data_, F.F_KEY AS name_, F.F_NAME AS desc_, F.G_KEY AS group_, F.F_ENABLED AS enabled_ " +
                    "FROM ALK_BATCH_FLOW_GRAPH D JOIN ALK_BATCH_FLOW F ON D.F_ID = F.F_ID";

    @Override
    protected List<RootModel> loadResources() {
        String sql = QUERY_SQL + " WHERE D.D_CURRENT = 1 AND D.D_STATUS = 'DEPLOY'";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, Collections.emptyMap());

        return list.stream().filter(Objects::nonNull).map(this::buildRootModel).collect(Collectors.toList());
    }

    @Override
    protected RootModel loadResource(String identity) {
        if (!StringUtils.hasLength(identity)) {
            throw new IllegalArgumentException("identity must not be empty");
        }
        Long flowId = Long.valueOf(identity);
        String sql = QUERY_SQL + " WHERE F.F_ID = :flowId AND D.D_CURRENT = 1 AND D.D_STATUS = 'SAVE'";
        Map<String, Object> params = new HashMap<>();
        params.put("flowId", flowId);
        Map<String, Object> result = this.jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
        if (result == null || result.get("data_") == null) {
            throw new IllegalArgumentException("No data found for flowId " + flowId);
        }
        return this.buildRootModel(result);
    }

    private RootModel buildRootModel(Map<String, Object> properties) {
        byte[] bytes = (byte[]) properties.remove("data_");
        String content = new String(bytes, StandardCharsets.UTF_8);
        JsonNode mxCellNodes = XmlUtil.getJsonNode(content).path("root").path("mxCell");
        List<JsonNode> list = new ArrayList<>();
        mxCellNodes.forEach(list::add);

        GraphContext context = new GraphContext(list);
        StartNodeModelParser parser = ModelParser.Type.START.getParser();
        RootModel rootModel = parser.parse(context, properties);
        ModelParser.execute(context, rootModel.getSteps());
        return rootModel;
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
