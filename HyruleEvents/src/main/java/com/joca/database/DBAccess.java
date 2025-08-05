package com.joca.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DBAccess<T> {
    protected Connection connection;

    public DBAccess(Connection connection) {
        this.connection = connection;
    }

    public abstract void insert(T entity) throws SQLException;
    public abstract void update(T entity) throws SQLException;
    public abstract void delete(String primaryKey) throws SQLException;

    /**
     * Metodo que permite obtener una entidad en base a la llave primaria
     * Debe usarse cuando la entidad tiene una sola llave primaria
     * @param primaryKey llave primaria de la entidad
     * @return entidad T
     * @throws SQLException si ocurre un error al realizar la operación
     */
    public abstract T findByKey(String primaryKey) throws  SQLException;

    /**
     * Obtiene todos los registros almacenados de una entidad
     * @return lista de entidades List<T>
     * @throws SQLException si ocurre un error al realizar la instrucción
     */
    public abstract List<T> findAll() throws SQLException;
}
