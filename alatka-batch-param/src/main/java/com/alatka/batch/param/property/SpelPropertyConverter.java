package com.alatka.batch.param.property;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class SpelPropertyConverter implements PropertyConverter {

    private static final String SPEL_PREFIX = PREFIX + "spel:";

    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public boolean matched(String value) {
        return value != null
                && value.startsWith(SPEL_PREFIX)
                && value.endsWith(PropertyConverter.SUFFIX);
    }

    @Override
    public String convert(String value) {
        String expression = this.formatProperty(value);
        StandardEvaluationContext context = new StandardEvaluationContext();
        try {
            return parser.parseExpression(expression).getValue(context, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse expression: " + expression, e);
        }
    }

    @Override
    public int order() {
        return 0;
    }

    private String formatProperty(String value) {
        return value.substring(SPEL_PREFIX.length(), value.length() - PropertyConverter.SUFFIX.length());
    }
}
