package com.alatka.batch.flow.model;

import com.alatka.batch.infra.util.JsonUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DecisionModel extends BeanComponentModel {

    @NotEmpty
    private List<InnerModel> decisions;

    public static class InnerModel {

        @NotBlank
        private String when;

        private Exit exit;

        private List<ComponentModel> to;

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

        public List<ComponentModel> getTo() {
            return to;
        }

        public void setOriginTo(List<ComponentModel> to) {
            this.to = to;
        }

        /**
         * Yaml工具解析为Map类型，需手动转换为List
         *
         * @param to Map<String, Map<String, Object>>类型
         */
        public void setTo(Map<String, Map<String, Object>> to) {
            this.to = to.values().stream().map(map -> {
                ComponentModel model = JsonUtil.convertToObject(map, ComponentModel.class);
                return JsonUtil.convertToObject(map, model.getType().getClazz());
            }).collect(Collectors.toList());
        }
    }

    public List<InnerModel> getDecisions() {
        return decisions;
    }

    public void setOriginDecisions(List<InnerModel> decisions) {
        this.decisions = decisions;
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
