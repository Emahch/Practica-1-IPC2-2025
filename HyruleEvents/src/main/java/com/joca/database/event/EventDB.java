package com.joca.database.event;

import com.joca.database.DBAccess;
import com.joca.database.OneKey;
import com.joca.model.event.Event;
import com.joca.model.event.EventTypeEnum;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDB extends DBAccess<Event> implements OneKey<Event> {

    public EventDB(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Event event) throws SQLException, NotRowsAffectedException {
        String query = "INSERT INTO event (id, title, date, type, location, max_capacity) VALUES" +
                "(?,?,?,?,?,?);";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, event.getId());
            st.setString(2,event.getTitle());
            st.setDate(3, Date.valueOf(event.getDate()));
            st.setString(4, event.getType().name());
            st.setString(5, event.getLocation());
            st.setInt(6,event.getMaxCapacity());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo registrar el evento con id: " + event.getId());
            }
        }
    }

    @Override
    public List<Event> findAll() throws SQLException, NotFoundException {
        String query = "SELECT * FROM event";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(query);
             ResultSet result = st.executeQuery()) {

            while (result.next()) {
                Event event = new Event();
                event.setId(result.getString("id"));
                event.setTitle(result.getString("title"));
                event.setLocation(result.getString("location"));
                event.setType(EventTypeEnum.valueOf(result.getString("type")));
                event.setDate(result.getDate("date").toLocalDate());
                event.setMaxCapacity(result.getInt("max_capacity"));
                events.add(event);
            }
        }
        if (events.isEmpty()) {
            throw new NotFoundException("No se hallaron eventos registrados");
        }
        return events;
    }

    @Override
    public Event findByKey(String primaryKey) throws SQLException, NotFoundException {
        String query = "SELECT * FROM event WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1,primaryKey);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    Event event = new Event();
                    event.setId(result.getString("id"));
                    event.setTitle(result.getString("title"));
                    event.setLocation(result.getString("location"));
                    event.setType(EventTypeEnum.valueOf(result.getString("type")));
                    event.setDate(result.getDate("date").toLocalDate());
                    event.setMaxCapacity(result.getInt("max_capacity"));
                    return event;
                }
            }
        }
        throw new NotFoundException("No se encontró el evento con el id: " + primaryKey);
    }

    @Override
    public void updateByKey(Event event, String primaryKey) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE event SET id = ?, title = ?, date = ?, type = ?, location = ?, max_capacity = ? " +
                "WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, event.getId());
            st.setString(2,event.getTitle());
            st.setDate(3, Date.valueOf(event.getDate()));
            st.setString(4, event.getType().name());
            st.setString(5, event.getLocation());
            st.setInt(6,event.getMaxCapacity());
            st.setString(7, primaryKey);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar el evento con id: " + primaryKey);
            }
        }
    }

    @Override
    public void deleteByKey(String primaryKey) throws SQLException, NotRowsAffectedException {
        String query = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, primaryKey);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo eliminar el evento con id: " + primaryKey);
            }
        }
    }

    @Override
    public boolean isKeyInUse(String primaryKey) throws SQLException {
        String query = "SELECT COUNT(*) AS c FROM event WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1,primaryKey);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                return result.getInt("c") > 0;
            }
            return true;
        }
    }
}
