package com.joca.model.registration;

import com.joca.database.event.EventDB;
import com.joca.model.DBEntity;
import com.joca.model.event.Event;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.participant.Participant;
import com.joca.model.registration.payment.Payment;

import java.util.Optional;

public class Registration extends DBEntity {

    private String participantEmail;
    private String eventId;
    private RegistrationTypeEnum type;
    private Optional<Payment> payment;
    private RegistrationStatusEnum status;

    public Registration() {
    }

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

    public RegistrationTypeEnum getType() {
        return type;
    }

    public void setType(RegistrationTypeEnum type) {
        this.type = type;
    }

    public Optional<Payment> getPayment() {
        return payment;
    }

    public void setPayment(Optional<Payment> payment) {
        this.payment = payment;
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
        validateNull(type, "un tipo de inscripción");
        if (payment.isPresent()) {
            payment.get().validate();
        }
    }

    @Override
    public String[] getAsRow() {
        return new String[]{
            eventId,
            participantEmail,
            type.name().toLowerCase(),
            payment.isPresent() ? payment.get().getMethod().name().toLowerCase() : "No pagado",
            payment.isPresent() ? String.valueOf(payment.get().getAmount()) : " - - ",
            status.name().toLowerCase()
        };
    }

}
