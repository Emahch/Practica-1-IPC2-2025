package com.joca.services;

import com.joca.database.event.EventDB;
import com.joca.database.registration.PaymentValidationDB;
import com.joca.model.event.Event;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.registration.RegistrationStatusEnum;
import com.joca.model.registration.ValidationDTO;
import com.joca.model.registration.payment.Payment;

import java.sql.SQLException;
import java.util.Optional;

public class PaymentValidationService {
    private PaymentValidationDB paymentValidationDB;

    public PaymentValidationService(PaymentValidationDB paymentValidationDB) {
        this.paymentValidationDB = paymentValidationDB;
    }

    public void updatePayment(Payment payment) throws SQLException, NotRowsAffectedException, InvalidRequisitesException {
        payment.validate();
        paymentValidationDB.updatePayment(payment);
    }

    public void updateValidation(ValidationDTO validationDTO) throws SQLException, NotRowsAffectedException, InvalidRequisitesException, NotFoundException {
        validationDTO.validate();
        if (validationDTO.getStatus().equals(RegistrationStatusEnum.VALIDADA)) {
            Payment payment = getPayment(validationDTO.getParticipantEmail(), validationDTO.getEventId());
            EventService eventService = new EventService(new EventDB());
            Event event = eventService.getEventByID(validationDTO.getEventId());
            if (payment.getAmount() < event.getPrice()) {
                throw new InvalidRequisitesException("No se puede validar la inscripción de " + validationDTO.getParticipantEmail() + " en el " +
                        "evento " + validationDTO.getEventId() + ". El pago no esta completo");
            }
        }
        paymentValidationDB.updateValidation(validationDTO);
    }

    public Payment getPayment(String participantEmail, String eventId) throws SQLException, NotFoundException {
        Optional<Payment> payment = paymentValidationDB.getPayment(participantEmail, eventId);
        if (payment.isEmpty()) {
            throw new NotFoundException("No se encontró información de pago del participante: " + participantEmail +
                    " en el evento: " + eventId);
        }
        return payment.get();
    }

    public ValidationDTO getValidationStatus(String participantEmail, String eventId) throws SQLException, NotFoundException {
        return paymentValidationDB.getValidation(participantEmail, eventId);
    }

    public void setPayment(Payment payment) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, NotFoundException, InvalidRequisitesException {
        payment.validate();
        if (paymentValidationDB.isPaymentPresent(payment.getParticipantEmail(), payment.getEventId())) {
            throw new DuplicatedKeyException("Ya existe un pago del participante: " + payment.getParticipantEmail() +
                    " en el evento: " + payment.getEventId());
        }
        updatePayment(payment);
    }

    public void deletePayment(String participantEmail, String eventID) throws SQLException, NotRowsAffectedException {
        Payment payment = new Payment();
        payment.setParticipantEmail(participantEmail);
        payment.setEventId(eventID);
        paymentValidationDB.updatePayment(payment);
    }

    public boolean isValidated(String participantEmail, String eventId) throws SQLException, NotFoundException {
        return paymentValidationDB.isValidated(participantEmail, eventId);
    }
}
