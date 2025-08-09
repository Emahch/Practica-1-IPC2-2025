package com.joca.database;

import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface MultiKey<T> {
    /**
     * Permite obtener información de la base de datos y filtrarla por sus atributos
     * @param attributes map atributos de la entidad
     * @return lista de entidades List<T>
     * @throws SQLException si ocurre un error al realizar la operación
     */
    List<T> findByAttributes(HashMap<String,String> attributes) throws SQLException, NotFoundException;

    /**
     * Permite eliminar un registro de la base de datos filtrada por sus atributos
     * @param attributes map atributos de la entidad
     * @throws SQLException si ocurre un error al realizar la operación
     */
    void deleteByAttributes(HashMap<String,String> attributes) throws SQLException;

    /**
     * Actualiza un registro de la base de datos, especificando sus llaves primarias originales
     * @param entity entidad actualizada
     * @param attributes llaves primarias originales
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotFoundException si no se encuentra la entidad con las llaves primarias originales
     */
    void updateByAttributes(T entity, HashMap<String,String> attributes) throws SQLException, NotFoundException;
}
