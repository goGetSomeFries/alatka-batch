package com.alatka.batch.flow.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DecisionModel extends BeanComponentModel {

    private List<InnerModel> decisions;

    public static class InnerModel {

        private String when;

        private String to;

        public static enum Status {
            failed, stopped, end
        }

        public String getWhen() {
            return when;
        }

        public void setWhen(String when) {
            this.when = when;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }

    public List<InnerModel> getDecisions() {
        return decisions;
    }

    /**
     * Yaml工具解析为Map类型，需手动转换为List
     *
     * @param decisions Map<String, InnerModel>类型
     */
    public void setDecisions(Map<String, InnerModel> decisions) {
        this.decisions = decisions.values().stream().collect(Collectors.toList());
    }
}
