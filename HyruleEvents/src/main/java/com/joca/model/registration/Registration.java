package com.joca.model.registration;

import com.joca.model.registration.payment.Payment;

import java.util.Optional;

public class Registration {
    private String participantEmail;
    private String eventId;
    private RegistrationTypeEnum type;
    private Optional<Payment> payment;
    private RegistrationStatusEnum status;

    public Registration() {}

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
}
