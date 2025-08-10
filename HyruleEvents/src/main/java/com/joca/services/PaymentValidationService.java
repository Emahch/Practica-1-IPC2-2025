package com.joca.services;

import com.joca.database.registration.PaymentValidationDB;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.registration.ValidationDTO;
import com.joca.model.registration.payment.Payment;

import java.sql.SQLException;
import java.util.Optional;

public class PaymentValidationService {
    private PaymentValidationDB paymentValidationDB;

    public PaymentValidationService(PaymentValidationDB paymentValidationDB) {
        this.paymentValidationDB = paymentValidationDB;
    }

    public void updatePayment(Payment payment) throws SQLException, NotRowsAffectedException {
        paymentValidationDB.updatePayment(payment);
    }

    public void updateValidation(ValidationDTO validationDTO) throws SQLException, NotRowsAffectedException {
        paymentValidationDB.updateValidation(validationDTO);
    }

    public Payment getPayment(String participantEmail, String eventId) throws SQLException, NotFoundException {
        Optional<Payment> payment = paymentValidationDB.getPayment(participantEmail,eventId);
        if (payment.isEmpty()) {
            throw new NotFoundException("No se encontró información de pago del participante: " + participantEmail +
                    " en el evento: " + eventId);
        }
        return payment.get();
    }

    public ValidationDTO getValidationStatus(String participantEmail, String eventId) throws SQLException, NotFoundException {
        return paymentValidationDB.getValidation(participantEmail,eventId);
    }

    public void setPayment(Payment payment) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, NotFoundException {
        if (paymentValidationDB.isPaymentPresent(payment.getParticipantEmail(), payment.getEventId())) {
            throw new DuplicatedKeyException("Ya existe un pago del participante: " + payment.getParticipantEmail() +
                    " en el evento: " + payment.getEventId());
        }
        updatePayment(payment);
    }

    public boolean isValidated(String participantEmail, String eventId) throws SQLException, NotFoundException {
        return paymentValidationDB.isValidated(participantEmail, eventId);
    }
}
