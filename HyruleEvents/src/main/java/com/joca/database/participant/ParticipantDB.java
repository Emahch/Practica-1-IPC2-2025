package com.joca.database.participant;

import com.joca.database.DBConnection;
import com.joca.database.OneKey;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterDTO;
import com.joca.model.participant.Participant;
import com.joca.model.participant.ParticipantTypeEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDB extends DBConnection implements OneKey<Participant> {
    @Override
    public Participant findByKey(String primaryKey) throws SQLException, NotFoundException {
        String query = "SELECT * FROM participant WHERE email = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, primaryKey);

            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    Participant participant = new Participant();
                    participant.setEmail(result.getString("email"));
                    participant.setName(result.getString("name"));
                    participant.setInstitution(result.getString("institution"));
                    participant.setType(ParticipantTypeEnum.valueOf(result.getString("type")));
                    return participant;
                }
            }
        }
        throw new NotFoundException("No se encontró el participante con el id: " + primaryKey);
    }

    @Override
    public void updateByKey(Participant participant, String originalKey) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE participant SET email = ?, name = ?, type = ?, institution = ? " +
                "WHERE email = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participant.getEmail());
            st.setString(2, participant.getName());
            st.setString(3, participant.getType().name());
            st.setString(4, participant.getInstitution());
            st.setString(5, originalKey);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar el participante con id: " + originalKey);
            }
        }
    }

    @Override
    public void deleteByKey(String primaryKey) throws SQLException, NotRowsAffectedException {
        String query = "DELETE FROM participant WHERE email = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, primaryKey);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo eliminar el participante con id: " + primaryKey);
            }
        }
    }

    @Override
    public boolean isKeyInUse(String primaryKey) throws SQLException {
        String query = "SELECT COUNT(*) AS c FROM participant WHERE email = ?";
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
    public void insert(Participant participant) throws SQLException, NotRowsAffectedException {
        String query = "INSERT INTO participant (email, name, type, institution) VALUES" +
                "(?,?,?,?);";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participant.getEmail());
            st.setString(2, participant.getName());
            st.setString(3, participant.getType().name());
            st.setString(4, participant.getInstitution());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo registrar el participante con email: " + participant.getEmail());
            }
        }
    }

    @Override
    public List<Participant> findAll() throws SQLException, NotFoundException {
        String query = "SELECT * FROM participant";
        List<Participant> participants = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query);
             ResultSet result = st.executeQuery()) {

            while (result.next()) {
                Participant participant = new Participant();
                participant.setEmail(result.getString("email"));
                participant.setName(result.getString("name"));
                participant.setInstitution(result.getString("institution"));
                participant.setType(ParticipantTypeEnum.valueOf(result.getString("type")));
                participants.add(participant);
            }
        }
        if (participants.isEmpty()) {
            throw new NotFoundException("No se hallaron participantes registrados");
        }
        return participants;
    }

    @Override
    public List<Participant> findByAttributes(List<Filter> filters) throws SQLException, NotFoundException {
        String query = "SELECT * FROM participant";
        FilterDTO filterDTO = processFilters(filters, query);
        List<Participant> participants = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(filterDTO.getQueryWithFilters())) {
            for (int i = 0; i < filterDTO.getValues().length; i++) {
                st.setObject(i + 1, filterDTO.getValues()[i]);
            }

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Participant participant = new Participant();
                    participant.setEmail(result.getString("email"));
                    participant.setName(result.getString("name"));
                    participant.setInstitution(result.getString("institution"));
                    participant.setType(ParticipantTypeEnum.valueOf(result.getString("type")));
                    participants.add(participant);
                }
            }
        }
        if (participants.isEmpty()) {
            throw new NotFoundException("No se hallaron participantes registrados en base al filtro");
        }
        return participants;
    }
}
