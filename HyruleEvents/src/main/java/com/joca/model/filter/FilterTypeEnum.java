package com.joca.model.filter;

public enum FilterTypeEnum {
    EQUAL("="),
    GREATER_THAN(">="),
    LESS_THAN("<=");

    private String operator;

    private FilterTypeEnum(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
