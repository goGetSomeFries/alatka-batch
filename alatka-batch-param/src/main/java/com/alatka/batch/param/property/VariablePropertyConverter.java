package com.alatka.batch.param.property;

public abstract class VariablePropertyConverter implements PropertyConverter {

    protected abstract String identity();

    @Override
    public boolean matched(String value) {
        return value != null
                && value.startsWith(PropertyConverter.PREFIX)
                && value.endsWith(PropertyConverter.SUFFIX)
                && this.formatProperty(value).equals(this.identity());
    }

    private String formatProperty(String value) {
        return value.substring(PropertyConverter.PREFIX.length(), value.length() - PropertyConverter.SUFFIX.length());
    }
}
