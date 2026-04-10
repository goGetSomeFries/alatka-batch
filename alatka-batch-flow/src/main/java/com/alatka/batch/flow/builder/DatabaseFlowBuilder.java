package com.alatka.batch.flow.builder;

import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.flow.parser.GraphContext;
import com.alatka.batch.flow.parser.ModelParser;
import com.alatka.batch.infra.util.XmlUtil;
import com.fasterxml.jackson.databind.JsonNode;
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

        return list.stream().filter(Objects::nonNull).map(bytes -> this.test(bytes, null)).collect(Collectors.toList());
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
        return this.test(bytes, null);
    }

    public static void main(String[] args) {
        String content = "<mxGraphModel><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/><mxCell id=\"18\" style=\"START\" vertex=\"1\" parent=\"1\"><Object label=\"Start\" type=\"START\" as=\"value\"/><mxGeometry x=\"130.00287683331612\" y=\"160.00311250109388\" width=\"70\" height=\"70\" as=\"geometry\"/></mxCell><mxCell id=\"19\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseData beanName=\"step_test1\" as=\"data\"/></Object><mxGeometry x=\"319.9987035362311\" y=\"179.9986426055923\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"20\" style=\"END\" vertex=\"1\" parent=\"1\"><Object label=\"End\" type=\"END\" as=\"value\"/><mxGeometry x=\"1409.9976742858748\" y=\"342.6662798802236\" width=\"60\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"21\" edge=\"1\" parent=\"1\" source=\"18\" target=\"19\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"25\" style=\"SPLIT\" vertex=\"1\" parent=\"1\"><Object label=\"Split\" type=\"SPLIT\" as=\"value\"/><mxGeometry x=\"339.9971607968585\" y=\"531.7258406106209\" width=\"60\" height=\"140\" as=\"geometry\"/></mxCell><mxCell id=\"27\" style=\"FLOW\" vertex=\"1\" parent=\"1\"><Object label=\"Flow\" type=\"FLOW\" as=\"value\"><BaseData beanName=\"flow_test3\" as=\"data\"/></Object><mxGeometry x=\"476.3172476655872\" y=\"690.0047663261705\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"28\" style=\"FLOW\" vertex=\"1\" parent=\"1\"><Object label=\"Flow\" type=\"FLOW\" as=\"value\"><BaseData beanName=\"flow_test2\" as=\"data\"/></Object><mxGeometry x=\"456.3205471194602\" y=\"479.9974218274462\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"29\" edge=\"1\" parent=\"1\" source=\"25\" target=\"28\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"30\" edge=\"1\" parent=\"1\" source=\"25\" target=\"27\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"31\" style=\"JOIN\" vertex=\"1\" parent=\"1\"><Object label=\"Join\" type=\"JOIN\" as=\"value\"/><mxGeometry x=\"726.3181459218979\" y=\"519.9955057616354\" width=\"60\" height=\"140\" as=\"geometry\"/></mxCell><mxCell id=\"32\" edge=\"1\" parent=\"1\" source=\"28\" target=\"31\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"33\" edge=\"1\" parent=\"1\" source=\"27\" target=\"31\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"34\" edge=\"1\" parent=\"1\" source=\"19\" target=\"25\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"35\" style=\"DECISION\" vertex=\"1\" parent=\"1\"><Object label=\"Decision\" type=\"DECISION\" as=\"value\"><BaseData beanName=\"defaultDecision1\" as=\"data\"/></Object><mxGeometry x=\"639.9997141218248\" y=\"289.99403326989545\" width=\"180\" height=\"90\" as=\"geometry\"/></mxCell><mxCell id=\"36\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseData beanName=\"step_test3\" as=\"data\"/></Object><mxGeometry x=\"934.8689138578036\" y=\"229.9967103107023\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"37\" style=\"FLOW\" vertex=\"1\" parent=\"1\"><Object label=\"Flow\" type=\"FLOW\" as=\"value\"><BaseData beanName=\"flow_test1\" as=\"data\"/></Object><mxGeometry x=\"1069.9977918010572\" y=\"380.0041485463175\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"38\" value=\"FAILED\" edge=\"1\" parent=\"1\" source=\"35\" target=\"37\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"39\" value=\"COMPLETED\" edge=\"1\" parent=\"1\" source=\"35\" target=\"36\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"40\" style=\"STEP\" vertex=\"1\" parent=\"1\"><Object label=\"Step\" type=\"STEP\" as=\"value\"><BaseData beanName=\"step_test3\" as=\"data\"/></Object><mxGeometry x=\"1149.9989138578035\" y=\"239.9967103107023\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"41\" edge=\"1\" parent=\"1\" source=\"36\" target=\"40\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"42\" edge=\"1\" parent=\"1\" source=\"40\" target=\"20\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"43\" edge=\"1\" parent=\"1\" source=\"31\" target=\"35\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"44\" edge=\"1\" parent=\"1\" source=\"37\" target=\"20\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></root></mxGraphModel>";
        new DatabaseFlowBuilder().test(content.getBytes(StandardCharsets.UTF_8), null);
    }

    private RootModel test(byte[] bytes, Map<String, Object> properties) {
        String content = new String(bytes, StandardCharsets.UTF_8);
        List<JsonNode> list = XmlUtil.getJsonNode(content).findValues("mxCell");

        GraphContext context = new GraphContext(list);
        ModelParser.execute(context, properties);
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
