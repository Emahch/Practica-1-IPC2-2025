package com.joca.model.registration.payment;

import com.joca.model.DBEntity;
import com.joca.model.event.Event;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.participant.Participant;

public class Payment extends DBEntity {
    private String participantEmail;
    private String eventId;
    private PaymentMethodEnum method;
    private double amount;

    public Payment() {
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

    public PaymentMethodEnum getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodEnum method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void validate() throws InvalidRequisitesException {
        validateString(participantEmail, Participant.MAX_LENGTH_EMAIL, "un email para el participante");
        validateString(eventId, Event.MAX_LENGTH_ID, "un código para el evento");
        validateNull(method, "un método de pago");
        validateDouble(amount, "una cantidad", true);
    }

    @Override
    public String[] getAsRow() {
        return new String[]{
            eventId,
            participantEmail,
            method.name().toLowerCase(),
            String.valueOf(amount)
        };
    }
}
