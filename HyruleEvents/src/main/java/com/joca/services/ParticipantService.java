package com.joca.services;

import com.joca.database.participant.ParticipantDB;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.participant.Participant;

import java.sql.SQLException;
import java.util.List;

public class ParticipantService {

    private ParticipantDB participantDB;

    public ParticipantService(ParticipantDB participantDB) {
        this.participantDB = participantDB;
    }

    public void createParticipant(Participant participant) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException {
        participant.validate();
        if (isKeyInUse(participant.getEmail())) {
            throw new DuplicatedKeyException("Error al crear el participante, el correo : " + participant.getEmail() + " ya esta en uso");
        }
        participantDB.insert(participant);
    }

    public Participant getParticipantByID(String participantID) throws SQLException, NotFoundException {
        return participantDB.findByKey(participantID);
    }

    public List<Participant> getAllParticipants() throws SQLException, NotFoundException {
        return participantDB.findAll();
    }

    public void updateParticipant(Participant participant, String originalParticipantEmail) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException {
        participant.validate();
        if (isKeyInUse(participant.getEmail()) && !participant.getEmail().equals(originalParticipantEmail)) {
            throw new DuplicatedKeyException("Error al actualizar el participante, el correo : " + participant.getEmail() + " ya esta en uso");
        }

        participantDB.updateByKey(participant, originalParticipantEmail);
    }

    public void deleteParticipant(String participantID) throws SQLException, NotRowsAffectedException {
        participantDB.deleteByKey(participantID);
    }

    public boolean isKeyInUse(String participantID) throws SQLException {
        return participantDB.isKeyInUse(participantID);
    }

    public List<Participant> getParticipantsByFilter(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        return participantDB.findByAttributes(filters);
    }
}
