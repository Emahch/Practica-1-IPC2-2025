package com.joca.database;

import com.joca.model.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.HashMap;

public interface DoubleKey<T> extends DBAccess<T> {

    /**
     * Permite eliminar un registro de la base de datos filtrada por sus atributos
     * @param key1 primera llave primaria
     * @param key2 segunda llave primaria
     * @throws SQLException si ocurre un error al realizar la operación
     */
    void deleteByKeys(String key1, String key2) throws SQLException;

    /**
     * Actualiza un registro de la base de datos, especificando sus llaves primarias originales
     * @param entity entidad actualizada
     * @param key1 primera llave primaria
     * @param key2 segunda llave primaria
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotFoundException si no se encuentra la entidad con las llaves primarias originales
     */
    void updateByKeys(T entity, String key1, String key2) throws SQLException, NotFoundException;

    /**
     * Devuelve true si se encuentra un registro existente en la base de datos con las llaves indicadas
     * @param key1 primera llave primaria
     * @param key2 segunda llave primaria
     * @return true si ya existe en la base de datos, false si no
     * @throws SQLException si ocurre un error al realizar la operación
     */
    boolean isKeysInUse(String key1, String key2) throws SQLException;
}
