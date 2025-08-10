package com.joca.database.registration;

import com.joca.database.DBConnection;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.registration.RegistrationStatusEnum;
import com.joca.model.registration.RegistrationTypeEnum;
import com.joca.model.registration.ValidationDTO;
import com.joca.model.registration.payment.Payment;
import com.joca.model.registration.payment.PaymentMethodEnum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

public class PaymentValidationDB extends DBConnection {

    public Optional<Payment> getPayment(String participantEmail, String eventId) throws SQLException, NotFoundException {
        String query = "SELECT payment_method, payment_amount FROM participant_event_registration" +
                " WHERE event_id = ? AND participant_email = ?";
        try (PreparedStatement st = getConnection().prepareStatement(query)) {
            st.setString(1, eventId);
            st.setString(2, participantEmail);

            try (ResultSet result = st.executeQuery()){
                if (!result.next()) {
                    throw new NotFoundException("No se encontraron datos de la inscripción del participante: "
                            + participantEmail + " en el evento: " + eventId);
                }
                Payment payment = new Payment();
                String posiblePaymentMethod = result.getString("payment_method");
                if (posiblePaymentMethod == null) {
                    return Optional.empty();
                }
                payment.setMethod(PaymentMethodEnum.valueOf(posiblePaymentMethod));
                payment.setAmount(result.getDouble("payment_amount"));
                payment.setEventId(eventId);
                payment.setParticipantEmail(participantEmail);
                return Optional.of(payment);
            }
        }
    }

    public void updatePayment(Payment payment) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE participant_event_registration SET payment_method = ?, " +
                "payment_amount = ? WHERE participant_email = ? AND event_id = ?";
        try (PreparedStatement st = getConnection().prepareStatement(query)) {
            if (payment.getMethod() == null) {
                st.setNull(1, Types.VARCHAR);
                st.setNull(2, Types.DOUBLE);
            } else {
                st.setString(1, payment.getMethod().name());
                st.setDouble(2, payment.getAmount());
            }
            st.setString(3, payment.getParticipantEmail());
            st.setString(4, payment.getEventId());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar el pago del participante: "
                        + payment.getParticipantEmail() + " en el evento: " + payment.getEventId());
            }
        }
    }

    public void updateValidation(ValidationDTO validationDTO) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE participant_event_registration SET status = ?" +
                " WHERE participant_email = ? AND event_id = ?";
        try (PreparedStatement st = getConnection().prepareStatement(query)) {
            st.setString(1, validationDTO.getStatus().name());
            st.setString(2, validationDTO.getParticipantEmail());
            st.setString(3, validationDTO.getEventId());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar el estado de validación del participante: "
                        + validationDTO.getParticipantEmail() + " en el evento: " + validationDTO.getEventId());
            }
        }
    }

    public ValidationDTO getValidation(String participantEmail, String eventId) throws SQLException, NotFoundException {
        String query = "SELECT status FROM participant_event_registration" +
                " WHERE event_id = ? AND participant_email = ?";
        try (PreparedStatement st = getConnection().prepareStatement(query)) {
            st.setString(1, eventId);
            st.setString(2, participantEmail);

            try (ResultSet result = st.executeQuery()){
                if (result.next()) {
                    ValidationDTO validationDTO = new ValidationDTO();
                    validationDTO.setStatus(RegistrationStatusEnum.valueOf(result.getString("status")));
                    validationDTO.setEventId(eventId);
                    validationDTO.setParticipantEmail(participantEmail);
                    return validationDTO;
                }
                throw new NotFoundException("No se encontraron datos de la inscripción del participante: "
                    + participantEmail + " en el evento: " + eventId);
            }
        }
    }

    public boolean isPaymentPresent(String participantEmail, String eventId) throws SQLException, NotFoundException {
        String query = "SELECT payment_method FROM participant_event_registration WHERE " +
                "participant_email = ? AND event_id = ?";
        try (PreparedStatement st = getConnection().prepareStatement(query)) {
            st.setString(1, participantEmail);
            st.setString(2, eventId);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                String payment = result.getString("payment_method");
                return payment != null && !payment.isBlank();
            }
            throw new NotFoundException("No se encontraron datos de la inscripción del participante: "
                    + participantEmail + " en el evento: " + eventId);
        }
    }

    public boolean isValidated(String participantEmail, String eventId) throws SQLException, NotFoundException {
        String query = "SELECT status FROM participant_event_registration WHERE " +
                "participant_email = ? AND event_id = ?";
        try (PreparedStatement st = getConnection().prepareStatement(query)) {
            st.setString(1, participantEmail);
            st.setString(2, eventId);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                RegistrationStatusEnum status = RegistrationStatusEnum.valueOf(result.getString("status"));
                return status.equals(RegistrationStatusEnum.VALIDADA);
            }
            throw new NotFoundException("No se encontraron datos de la inscripción del participante: "
                    + participantEmail + " en el evento: " + eventId);
        }
    }
}
