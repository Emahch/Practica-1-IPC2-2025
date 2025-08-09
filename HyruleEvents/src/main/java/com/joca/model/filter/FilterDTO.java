package com.joca.model.filter;

public class FilterDTO {
    private Object[] values;
    private String queryWithFilters;

    public FilterDTO(Object[] values, String queryWithFilters) {
        this.values = values;
        this.queryWithFilters = queryWithFilters;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getQueryWithFilters() {
        return queryWithFilters;
    }

    public void setQueryWithFilters(String queryWithFilters) {
        this.queryWithFilters = queryWithFilters;
    }
}
