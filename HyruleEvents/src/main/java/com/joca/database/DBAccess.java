package com.joca.database;

import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface DBAccess<T> {

    /**
     * Inserta un registro nuevo en la base de datos
     * @param entity entidad a guardar
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotRowsAffectedException si no se logra ingresar el nuevo registro
     */
    void insert(T entity) throws SQLException, NotRowsAffectedException;

    /**
     * Obtiene todos los registros almacenados de una entidad
     * @return lista de entidades List<T>
     * @throws SQLException si ocurre un error al realizar la instrucción
     * @throws NotFoundException si no encuentra ningun registro
     */
    List<T> findAll() throws SQLException, NotFoundException;

    /**
     * Permite obtener información de la base de datos y filtrarla por sus atributos
     * @param attributes map atributos de la entidad
     * @return lista de entidades List<T>
     * @throws SQLException si ocurre un error al realizar la operación
     */
    List<T> findByAttributes(HashMap<String,String> attributes) throws SQLException, NotFoundException;
}
