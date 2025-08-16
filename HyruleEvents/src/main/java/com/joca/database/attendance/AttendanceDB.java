package com.joca.database.attendance;

import com.joca.database.DBConnection;
import com.joca.database.DoubleKey;
import com.joca.model.attendance.Attendance;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDB extends DBConnection implements DoubleKey<Attendance> {
    @Override
    public void deleteByKeys(String participantEmail, String activityId) throws SQLException, NotRowsAffectedException {
        String query = "DELETE FROM participant_activity_attendance WHERE participant_email = ? AND activity_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participantEmail);
            st.setString(2, activityId);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo eliminar la asistencia del participante: " + participantEmail + " en la actividad: " + activityId);
            }
        }
    }

    @Override
    public void updateByKeys(Attendance attendance, String participantEmail, String activityId) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE participant_activity_attendance SET participant_email = ?, activity_id = ? " +
                " WHERE participant_email = ? AND event_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, attendance.getParticipantEmail());
            st.setString(2, attendance.getActivityId());
            st.setString(3, participantEmail);
            st.setString(4, activityId);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar la asistencia del participante: " + participantEmail + " en la actividad: " + activityId);
            }
        }
    }

    @Override
    public boolean isKeysInUse(String participantEmail, String activityId) throws SQLException {
        String query = "SELECT COUNT(*) AS c FROM participant_activity_attendance WHERE participant_email = ? AND activity_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participantEmail);
            st.setString(2, activityId);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                return result.getInt("c") > 0;
            }
            return true;
        }
    }

    @Override
    public void insert(Attendance attendance) throws SQLException, NotRowsAffectedException {
        String query = "INSERT INTO participant_activity_attendance (participant_email, activity_id)" +
                " VALUES (?,?)";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, attendance.getParticipantEmail());
            st.setString(2, attendance.getActivityId());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo registrar la asistencia del participante: "
                        + attendance.getParticipantEmail() + " en la actividad: " + attendance.getActivityId());
            }
        }
    }

    @Override
    public List<Attendance> findAll() throws SQLException, NotFoundException {
        String query = "SELECT * FROM participant_activity_attendance";
        List<Attendance> attendances = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query);
             ResultSet result = st.executeQuery()) {

            while (result.next()) {
                Attendance attendance = new Attendance(
                        result.getString("participant_email"),
                        result.getString("activity_id"));
                attendances.add(attendance);
            }
        }
        if (attendances.isEmpty()) {
            throw new NotFoundException("No se hallaron asistencias registradas");
        }
        return attendances;
    }

    @Override
    public List<Attendance> findByAttributes(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        String query = "SELECT * FROM participant_activity_attendance";
        List<Attendance> attendances = new ArrayList<>();
        FilterDTO filterDTO = processFilters(filters, query);

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(filterDTO.getQueryWithFilters())) {
            for (int i = 0; i < filterDTO.getValues().length; i++) {
                st.setObject(i + 1, filterDTO.getValues()[i]);
            }
            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Attendance attendance = new Attendance(
                            result.getString("participant_email"),
                            result.getString("activity_id"));
                    attendances.add(attendance);
                }
            }
        }
        if (attendances.isEmpty()) {
            throw new NotFoundException("No se hallaron asistencias registradas con el filtro especificado");
        }
        return attendances;
    }

    public boolean canGenerateCertificate(String participantEmail, String eventId) throws SQLException {
        String query = "SELECT COUNT(*) as c FROM participant_activity_attendance" +
                " JOIN activity ON activity_id = activity.id" +
                " WHERE activity.event_id = ? AND participant_activity_attendance.participant_email = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, eventId);
            st.setString(2, participantEmail);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    return result.getInt("c") > 0;
                }
                return false;
            }
        }
    }

    public int getAttendancesQuantity(String activityId) throws SQLException {
        String query = "SELECT COUNT(*) as c FROM participant_activity_attendance" +
                " WHERE activity_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, activityId);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    return result.getInt("c");
                }
                return 0;
            }
        }
    }
}
