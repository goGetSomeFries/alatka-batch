package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.Job;

public interface IComponent {

    Wrapper join(ComponentModel model, Object builder, Object lastOne);

    Job build(Object builder);

    boolean matched(ComponentModel model);

    class Wrapper {

        private Object builder;

        private Object lastOne;

        public Wrapper(Object builder, Object lastOne) {
            this.builder = builder;
            this.lastOne = lastOne;
        }

        public Object getBuilder() {
            return builder;
        }

        public Object getLastOne() {
            return lastOne;
        }
    }

}
