package com.joca.database;

import com.joca.model.exceptions.NotRowsAffectedException;

import java.sql.SQLException;

public interface DoubleKey<T> extends DBAccess<T> {

    /**
     * Permite eliminar un registro de la base de datos filtrada por sus atributos
     * @param key1 primera llave primaria
     * @param key2 segunda llave primaria
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotRowsAffectedException si no se ejecutan cambios (no se realizó la operación)
     */
    void deleteByKeys(String key1, String key2) throws SQLException, NotRowsAffectedException;

    /**
     * Actualiza un registro de la base de datos, especificando sus llaves primarias originales
     * @param entity entidad actualizada
     * @param key1 primera llave primaria
     * @param key2 segunda llave primaria
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotRowsAffectedException si no se ejecuta la operación
     */
    void updateByKeys(T entity, String key1, String key2) throws SQLException, NotRowsAffectedException;

    /**
     * Devuelve true si se encuentra un registro existente en la base de datos con las llaves indicadas
     * @param key1 primera llave primaria
     * @param key2 segunda llave primaria
     * @return true si ya existe en la base de datos, false si no
     * @throws SQLException si ocurre un error al realizar la operación
     */
    boolean isKeysInUse(String key1, String key2) throws SQLException;
}
