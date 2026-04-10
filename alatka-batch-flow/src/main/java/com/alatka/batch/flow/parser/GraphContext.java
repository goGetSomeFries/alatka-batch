package com.alatka.batch.flow.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphContext {

    private Map<String, List<JsonNode>> edgeMap;
    private Map<String, JsonNode> vertexMap;
    private JsonNode startNode;
    private AtomicReference<JsonNode> currentNodeRef = new AtomicReference<>();

    public GraphContext(List<JsonNode> list) {
        this.edgeMap = list.stream()
                .filter(node -> node.has("edge"))
                .collect(Collectors.groupingBy(node -> node.get("source").asText()));
        this.vertexMap = list.stream()
                .filter(node -> node.has("vertex"))
                .collect(Collectors.toMap(node -> node.get("id").asText(), Function.identity()));
        this.startNode = list.stream()
                .filter(node -> "START".equals(node.get("style").asText()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("'START' node must be exist"));
        this.currentNodeRef.set(this.startNode);
    }

    public JsonNode getCurrentNode() {
        return this.currentNodeRef.get();
    }

    public JsonNode resetCurrentNode(JsonNode node) {
        this.currentNodeRef.set(node);
        return this.currentNodeRef.get();
    }

    public JsonNode nextVertex() {
        return this.nextVertex(this.currentNodeRef.get());
    }

    public JsonNode nextVertex(JsonNode currentNode) {
        Stream<JsonNode> stream = currentNode.has("vertex")
                ? this.nextEdges(currentNode) : Stream.of(currentNode);
        JsonNode nextNode = stream
                .map(edge -> edge.get("target").asText())
                .map(id -> this.vertexMap.get(id))
                .peek(this.currentNodeRef::set)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such vertex"));
        return nextNode;
    }

    public Stream<JsonNode> nextEdges() {
        JsonNode currentNode = this.currentNodeRef.get();
        if (!currentNode.has("vertex")) {
            throw new IllegalArgumentException("current node:" + currentNode + " must be vertex");
        }
        return this.nextEdges(currentNode);
    }

    public Stream<JsonNode> nextEdges(JsonNode currentVertex) {
        return this.edgeMap.get(currentVertex.get("id").asText()).stream()
                .peek(this.currentNodeRef::set);
    }

}
