package com.joca.database.activity;

import com.joca.database.DBConnection;
import com.joca.database.OneKey;
import com.joca.model.activity.Activity;
import com.joca.model.activity.ActivityTypeEnum;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDB extends DBConnection implements OneKey<Activity> {
    @Override
    public Activity findByKey(String primaryKey) throws SQLException, NotFoundException {
        String query = "SELECT * FROM activity WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, primaryKey);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    Activity activity = new Activity();
                    activity.setId(result.getString("id"));
                    activity.setTitle(result.getString("title"));
                    activity.setEventID(result.getString("event_id"));
                    activity.setSpeakerEmail(result.getString("speaker_email"));
                    activity.setType(ActivityTypeEnum.valueOf(result.getString("type")));
                    activity.setMaxCapacity(result.getInt("max_capacity"));
                    activity.setStartTime(result.getTime("start_time").toLocalTime());
                    activity.setEndTime(result.getTime("end_time").toLocalTime());
                    return activity;
                }
            }
        }
        throw new NotFoundException("No se encontró la actividad con código: " + primaryKey);
    }

    @Override
    public void updateByKey(Activity activity, String primaryKey) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE activity SET id = ?, event_id = ?, title = ?, type = ?, speaker_email = ?, " +
                "start_time = ?, end_time = ?, max_capacity = ? WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, activity.getId());
            st.setString(2, activity.getEventID());
            st.setString(3, activity.getTitle());
            st.setString(4, activity.getType().name());
            st.setString(5, activity.getSpeakerEmail());
            st.setTime(6, Time.valueOf(activity.getStartTime()));
            st.setTime(7, Time.valueOf(activity.getEndTime()));
            st.setInt(8, activity.getMaxCapacity());
            st.setString(9, primaryKey);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar la actividad con id: " + primaryKey);
            }
        }
    }

    @Override
    public void deleteByKey(String primaryKey) throws SQLException, NotRowsAffectedException {
        String query = "DELETE FROM activity WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, primaryKey);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo eliminar la actividad con id: " + primaryKey);
            }
        }
    }

    @Override
    public boolean isKeyInUse(String primaryKey) throws SQLException {
        String query = "SELECT COUNT(*) AS c FROM activity WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, primaryKey);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                return result.getInt("c") > 0;
            }
            return true;
        }
    }

    @Override
    public void insert(Activity activity) throws SQLException, NotRowsAffectedException {
        String query = "INSERT INTO activity (id, event_id, title, type, speaker_email, start_time, end_time, max_capacity) VALUES" +
                "(?,?,?,?,?,?,?,?);";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, activity.getId());
            st.setString(2, activity.getEventID());
            st.setString(3, activity.getTitle());
            st.setString(4, activity.getType().name());
            st.setString(5, activity.getSpeakerEmail());
            st.setTime(6, Time.valueOf(activity.getStartTime()));
            st.setTime(7, Time.valueOf(activity.getEndTime()));
            st.setInt(8, activity.getMaxCapacity());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo registrar la actividad con id: " + activity.getId());
            }
        }
    }

    @Override
    public List<Activity> findAll() throws SQLException, NotFoundException {
        String query = "SELECT * FROM activity";
        List<Activity> activities = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query);
             ResultSet result = st.executeQuery()) {

            while (result.next()) {
                Activity activity = new Activity();
                activity.setId(result.getString("id"));
                activity.setTitle(result.getString("title"));
                activity.setEventID(result.getString("event_id"));
                activity.setSpeakerEmail(result.getString("speaker_email"));
                activity.setType(ActivityTypeEnum.valueOf(result.getString("type")));
                activity.setMaxCapacity(result.getInt("max_capacity"));
                activity.setStartTime(result.getTime("start_time").toLocalTime());
                activity.setEndTime(result.getTime("end_time").toLocalTime());
                activities.add(activity);
            }
        }
        if (activities.isEmpty()) {
            throw new NotFoundException("No se hallaron actividades registradas");
        }
        return activities;
    }

    @Override
    public List<Activity> findByAttributes(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        String query = "SELECT * FROM activity";
        FilterDTO filterDTO = processFilters(filters, query);
        List<Activity> activities = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(filterDTO.getQueryWithFilters())) {
            for (int i = 0; i < filterDTO.getValues().length; i++) {
                st.setObject(i + 1, filterDTO.getValues()[i]);
            }

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Activity activity = new Activity();
                    activity.setId(result.getString("id"));
                    activity.setTitle(result.getString("title"));
                    activity.setEventID(result.getString("event_id"));
                    activity.setSpeakerEmail(result.getString("speaker_email"));
                    activity.setType(ActivityTypeEnum.valueOf(result.getString("type")));
                    activity.setMaxCapacity(result.getInt("max_capacity"));
                    activity.setStartTime(result.getTime("start_time").toLocalTime());
                    activity.setEndTime(result.getTime("end_time").toLocalTime());
                    activities.add(activity);
                }
            }
        }
        if (activities.isEmpty()) {
            throw new NotFoundException("No se hallaron actividades registradas en base al filtro");
        }
        return activities;
    }
}

