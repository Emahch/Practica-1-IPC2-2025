package com.joca.database;

import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DBAccess<T> {
    protected Connection connection;

    public DBAccess(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserta un registro nuevo en la base de datos
     * @param entity entidad a guardar
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotRowsAffectedException si no se logra ingresar el nuevo registro
     */
    public abstract void insert(T entity) throws SQLException, NotRowsAffectedException;

    /**
     * Obtiene todos los registros almacenados de una entidad
     * @return lista de entidades List<T>
     * @throws SQLException si ocurre un error al realizar la instrucción
     * @throws NotFoundException si no encuentra ningun registro
     */
    public abstract List<T> findAll() throws SQLException, NotFoundException;
}
