package com.alatka.batch.infra.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;

import java.io.IOException;
import java.util.Map;

public class XmlUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new XmlFactory());

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Map<String, Object> getMap(String content) {
        return getMap(content, Object.class);
    }

    public static <T> Map<String, T> getMap(String content, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readerForMapOf(clazz).readValue(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String content = "<mxGraphModel><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/><mxCell id=\"2\" style=\"STEP\" parent=\"1\" vertex=\"1\"><StepNode as=\"value\"><BaseData beanName=\"\" as=\"data\"/></StepNode><mxGeometry x=\"729.9992920147957\" y=\"275.0038089092184\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"3\" style=\"START\" parent=\"1\" vertex=\"1\"><StartNode as=\"value\"/><mxGeometry x=\"149.9959734296541\" y=\"159.9992260905392\" width=\"70\" height=\"70\" as=\"geometry\"/></mxCell><mxCell id=\"4\" value=\"\" parent=\"1\" source=\"3\" target=\"8\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"6\" style=\"END\" parent=\"1\" vertex=\"1\"><EndNode as=\"value\"/><mxGeometry x=\"1279.995625\" y=\"275\" width=\"60\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"8\" style=\"DECISION\" parent=\"1\" vertex=\"1\"><DecisionNode as=\"value\"><BaseData as=\"data\"/></DecisionNode><mxGeometry x=\"309.9979256185087\" y=\"259.9955450084524\" width=\"180\" height=\"90\" as=\"geometry\"/></mxCell><mxCell id=\"9\" parent=\"1\" source=\"8\" target=\"2\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"11\" parent=\"1\" source=\"8\" target=\"28\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"12\" style=\"FLOW\" parent=\"1\" vertex=\"1\"><FlowNode as=\"value\"><BaseData as=\"data\"/></FlowNode><mxGeometry x=\"819.999038777635\" y=\"479.99917888111054\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"15\" style=\"FLOW\" parent=\"1\" vertex=\"1\"><FlowNode as=\"value\"><BaseData as=\"data\"/></FlowNode><mxGeometry x=\"819.995625\" y=\"635\" width=\"140\" height=\"60\" as=\"geometry\"/></mxCell><mxCell id=\"18\" parent=\"1\" source=\"12\" target=\"27\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"19\" parent=\"1\" source=\"15\" target=\"27\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"21\" parent=\"1\" source=\"2\" target=\"6\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"26\" parent=\"1\" source=\"27\" target=\"6\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"27\" style=\"JOIN\" parent=\"1\" vertex=\"1\"><JoinNode as=\"value\"/><mxGeometry x=\"1150.0038126380625\" y=\"510.001927524847\" width=\"60\" height=\"140\" as=\"geometry\"/></mxCell><mxCell id=\"28\" style=\"SPLIT\" parent=\"1\" vertex=\"1\"><SplitNode as=\"value\"/><mxGeometry x=\"370.000597969967\" y=\"520.0034315808786\" width=\"60\" height=\"140\" as=\"geometry\"/></mxCell><mxCell id=\"29\" parent=\"1\" source=\"28\" target=\"15\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"30\" parent=\"1\" source=\"28\" target=\"12\" edge=\"1\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></root></mxGraphModel>";
        Map<String, Object> map = getMap(content, Object.class);
        System.out.println(map);
    }
}
