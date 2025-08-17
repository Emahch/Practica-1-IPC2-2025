package com.joca.model.registration;

import com.joca.model.DBEntity;
import com.joca.model.event.Event;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.participant.Participant;

public class ValidationDTO extends DBEntity {
    private String participantEmail;
    private String eventId;
    private RegistrationStatusEnum status;

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public RegistrationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatusEnum status) {
        this.status = status;
    }

    @Override
    public void validate() throws InvalidRequisitesException {
        validateString(participantEmail, Participant.MAX_LENGTH_EMAIL, "un email para el participante");
        validateString(eventId, Event.MAX_LENGTH_ID, "un código para el evento");
        validateNull(status, "un estado");
    }

    @Override
    public String[] getAsRow() {
        return new String[]{
            eventId,
            participantEmail,
            status.name().toLowerCase()
        };
    }
}
