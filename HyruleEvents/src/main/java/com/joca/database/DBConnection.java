package com.joca.database;

import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/HyruleEvents";
    public static final String USER_NAME = "rootx";
    public static final String PASSWORD = "password1234";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

    protected FilterDTO processFilters(List<Filter> filters, String queryWithoutFilters) {
        StringBuilder sql = new StringBuilder(queryWithoutFilters);
        List<Object> values = new ArrayList<>();
        if (filters != null && !filters.isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = new ArrayList<>();
            for (Filter filter : filters) {
                conditions.add(filter.getColumnName() + " " + filter.getType().getOperator() + " ?");
                values.add(filter.getValue());
            }
            sql.append(String.join(" AND ", conditions));
        }

        return new FilterDTO(values.toArray(), sql.toString());
    }
}
