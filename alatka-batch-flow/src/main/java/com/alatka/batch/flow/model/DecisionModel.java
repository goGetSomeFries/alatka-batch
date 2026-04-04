package com.alatka.batch.flow.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DecisionModel extends BeanComponentModel {

    private List<InnerModel> decisions;

    public static class InnerModel {

        private String when;

        private Exit exit;

        private List<Map<RootModel.Type, Map<String, Object>>> to;

        public enum Exit {
            failed, stopped, end
        }

        public String getWhen() {
            return when;
        }

        public void setWhen(String when) {
            this.when = when;
        }

        public Exit getExit() {
            return exit;
        }

        public void setExit(Exit exit) {
            this.exit = exit;
        }

        public List<Map<RootModel.Type, Map<String, Object>>> getTo() {
            return to;
        }

        public void setTo(Map<String,Map<RootModel.Type, Map<String, Object>>> to) {
            this.to = to.values().stream().collect(Collectors.toList());
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
