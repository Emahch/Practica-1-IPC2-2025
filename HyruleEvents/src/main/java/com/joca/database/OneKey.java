package com.joca.database;

import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;

import java.sql.SQLException;

public interface OneKey<T> extends DBAccess<T> {
    /**
     * Permite obtener una entidad con base en la llave primaria
     * Debe usarse cuando la entidad tiene una sola llave primaria
     *
     * @param primaryKey llave primaria de la entidad
     * @return entidad T
     * @throws SQLException      si ocurre un error al realizar la operación
     * @throws NotFoundException si no se encuentra ningun registro con esa llave primaria
     */
    T findByKey(String primaryKey) throws SQLException, NotFoundException;

    /**
     * Actualiza un registro en la base de datos
     * Debe usarse cuando la entidad tiene una sola llave primaria
     *
     * @param entity     entidad con valores actualizados
     * @param primaryKey llave primaria original
     * @throws SQLException             si ocurre un error al ejecutar la operación
     * @throws NotRowsAffectedException si no se elimina el registro
     */
    void updateByKey(T entity, String primaryKey) throws SQLException, NotRowsAffectedException;


    /**
     * Permite eliminar una entidad con base en la llave primaria
     * Debe usarse cuando la entidad tiene una sola llave primaria
     *
     * @param primaryKey llave primaria de la entidad
     * @throws SQLException si ocurre un error al realizar la operación
     */
    void deleteByKey(String primaryKey) throws SQLException, NotRowsAffectedException;

    /**
     * Devuelve true si se encuentra un registro existente en la base de datos con la llave indicada
     *
     * @param primaryKey llave primaria de la entidad
     * @return true si ya existe en la base de datos, false si no
     * @throws SQLException si ocurre un error al realizar la operación
     */
    boolean isKeyInUse(String primaryKey) throws SQLException;
}
