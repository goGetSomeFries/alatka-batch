package com.alatka.batch.flow.model;

import java.util.List;

public class DecisionModel extends BeanComponentModel {

    private List<InnerModel> decisions;

    public static class InnerModel {

        private String on;

        private String to;

        public String getOn() {
            return on;
        }

        public void setOn(String on) {
            this.on = on;
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

    public void setDecisions(List<InnerModel> decisions) {
        this.decisions = decisions;
    }
}
