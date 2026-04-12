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

    private static final String QUERY_SQL = "SELECT D.D_DATA AS data, F.F_KEY AS key, F.F_NAME AS name " +
            "FROM ALK_BATCH_FLOW_GRAPH D JOIN ALK_BATCH_FLOW F ON D.F_ID = F.F_ID AND F.F_ENABLED = 1";

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
        Long id = Long.valueOf(identity);
        String sql = QUERY_SQL + " WHERE D.ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Map<String, Object> result = this.jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
        if (result == null || result.get("data") == null) {
            throw new IllegalArgumentException("No data found for id " + id);
        }
        return this.buildRootModel(result);
    }

    public static void main(String[] args) {
        String content = "<mxGraphModel><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/><mxCell id=\"14\" style=\"START\" vertex=\"1\" parent=\"1\"><Object label=\"Start\" type=\"START\" as=\"value\"/><mxGeometry x=\"219.9998064548995\" y=\"149.998984722072\" width=\"70\" height=\"70\" as=\"geometry\"/></mxCell><mxCell id=\"15\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseNodeData beanName=\"step_test1\" as=\"data\"/></Object><mxGeometry x=\"379.9995313176455\" y=\"160.00179160311524\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"26\" style=\"END\" vertex=\"1\" parent=\"1\"><Object label=\"End\" type=\"END\" as=\"value\"/><mxGeometry x=\"499.9965011531342\" y=\"690.0027839647847\" width=\"60\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"28\" style=\"DECISION\" vertex=\"1\" parent=\"1\"><Object label=\"Decision\" type=\"DECISION\" as=\"value\"><BaseNodeData beanName=\"defaultDecision1\" as=\"data\"/></Object><mxGeometry x=\"599.9910223415039\" y=\"290.0005972907687\" width=\"180\" height=\"90\" as=\"geometry\"/></mxCell><mxCell id=\"29\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseNodeData beanName=\"step_test3\" as=\"data\"/></Object><mxGeometry x=\"302.015625\" y=\"481\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"30\" edge=\"1\" parent=\"1\" source=\"14\" target=\"15\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"31\" edge=\"1\" parent=\"1\" source=\"15\" target=\"28\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"32\" value=\"COMPELTED\" edge=\"1\" parent=\"1\" source=\"28\" target=\"29\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"34\" style=\"FAIL\" vertex=\"1\" parent=\"1\"><Object label=\"Fail\" type=\"FAIL\" as=\"value\"/><mxGeometry x=\"889.995625\" y=\"480\" width=\"60\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"35\" value=\"STOPPED\" edge=\"1\" parent=\"1\" source=\"28\" target=\"39\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"36\" value=\"FAILED\" edge=\"1\" parent=\"1\" source=\"28\" target=\"34\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"37\" edge=\"1\" parent=\"1\" source=\"40\" target=\"26\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"38\" edge=\"1\" parent=\"1\" source=\"39\" target=\"26\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"39\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseNodeData beanName=\"step_test2\" as=\"data\"/></Object><mxGeometry x=\"679.997007843816\" y=\"580.0017774988363\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"40\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseNodeData beanName=\"step_test4\" as=\"data\"/></Object><mxGeometry x=\"279.9984181994323\" y=\"630.0012867108069\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"41\" edge=\"1\" parent=\"1\" source=\"29\" target=\"40\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></root></mxGraphModel>";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", content.getBytes(StandardCharsets.UTF_8));
        properties.put("key", "job_test1");
        properties.put("name", "测试job");
        properties.put("enabled", true);
        new DatabaseFlowBuilder().buildRootModel(properties);
    }

    private RootModel buildRootModel(Map<String, Object> properties) {
        byte[] bytes = (byte[]) properties.remove("data");
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
