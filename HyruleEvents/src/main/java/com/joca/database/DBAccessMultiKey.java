package com.joca.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public abstract class DBAccessMultiKey<T> extends DBAccess<T> {

    public DBAccessMultiKey(Connection connection) {
        super(connection);
    }

    /**
     * Permite obtener información de la base de datos y filtrarla por sus atributos
     * @param attributes map atributos de la entidad
     * @return lista de entidades List<T>
     * @throws SQLException si ocurre un error al realizar la operación
     */
    public abstract List<T> findByAttributes(HashMap<String,String> attributes) throws SQLException;

    /**
     * Permite eliminar un registro de la base de datos filtrada por sus atributos
     * @param attributes map atributos de la entidad
     * @throws SQLException si ocurre un error al realizar la operación
     */
    protected abstract void safeDeleteByAttributes(HashMap<String,String> attributes) throws SQLException;

    /**
     * Permite eliminar un registro de la base de datos filtrada por sus atributos si no están vacios
     * @param attributes map atributos de la entidad
     * @throws SQLException si ocurre un error al realizar la operación
     */
    public void deleteByAttributes(HashMap<String,String> attributes) throws SQLException {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        safeDeleteByAttributes(attributes);
    }


    // Se debe usar el nuevo metodo findByAttributes(...)
    @Override
    @Deprecated
    public abstract T findByKey(String primaryKey) throws SQLException;
}
