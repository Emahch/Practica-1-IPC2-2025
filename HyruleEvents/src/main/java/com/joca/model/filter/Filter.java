package com.joca.model.filter;

public class Filter {
    private String columnName;
    private Object value;
    private FilterTypeEnum type;

    public Filter(String columnName, Object value, FilterTypeEnum type) {
        this.columnName = columnName;
        this.value = value;
        this.type = type;
    }

    public Filter() {
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public FilterTypeEnum getType() {
        return type;
    }

    public void setType(FilterTypeEnum type) {
        this.type = type;
    }
}
