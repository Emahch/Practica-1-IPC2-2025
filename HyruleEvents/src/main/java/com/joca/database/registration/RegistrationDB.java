package com.joca.database.registration;

import com.joca.database.DBConnection;
import com.joca.database.DoubleKey;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterDTO;
import com.joca.model.registration.Registration;
import com.joca.model.registration.RegistrationStatusEnum;
import com.joca.model.registration.RegistrationTypeEnum;
import com.joca.model.registration.payment.Payment;
import com.joca.model.registration.payment.PaymentMethodEnum;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RegistrationDB extends DBConnection implements DoubleKey<Registration> {
    /**
     * Elimina una inscripción de la base de datos
     *
     * @param participantEmail email del participante
     * @param eventId código del evento
     * @throws SQLException si ocurre un error al realizar la operación
     * @throws NotRowsAffectedException si no se realiza la operación
     */
    @Override
    public void deleteByKeys(String participantEmail, String eventId) throws SQLException, NotRowsAffectedException {
        String query = "DELETE FROM participant_event_registration WHERE participant_email = ? AND event_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participantEmail);
            st.setString(2, eventId);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo eliminar la inscripción del participante: " + participantEmail + " en el evento: " + eventId);
            }
        }
    }

    /**
     * Actualiza una inscripción en la base de datos
     *
     * @param registration     entidad actualizada
     * @param participantEmail email original del participante
     * @param eventId          código original del evento
     * @throws SQLException             si ocurre un error al realizar la operación
     * @throws NotRowsAffectedException si no se ejecuta la operación
     */
    @Override
    public void updateByKeys(Registration registration, String participantEmail, String eventId) throws SQLException, NotRowsAffectedException {
        String query = "UPDATE participant_event_registration SET participant_email = ?, event_id = ?, type = ?," +
                " WHERE participant_email = ? AND event_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, registration.getParticipantEmail());
            st.setString(2, registration.getEventId());
            st.setString(3, registration.getType().name());
            st.setString(4, participantEmail);
            st.setString(5, eventId);

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo actualizar la inscripción del participante: " + participantEmail + " en el evento: " + eventId);
            }
        }
    }

    /**
     * Permite saber si la llave primaria ya está en uso
     *
     * @param participantEmail email del participante
     * @param eventId          código del participante
     * @return true si ya esta en uso
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean isKeysInUse(String participantEmail, String eventId) throws SQLException {
        String query = "SELECT COUNT(*) AS c FROM participant_event_registration WHERE participant_email = ? AND event_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participantEmail);
            st.setString(2, eventId);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                return result.getInt("c") > 0;
            }
            return true;
        }
    }

    @Override
    public void insert(Registration registration) throws SQLException, NotRowsAffectedException {
        String query = "INSERT INTO participant_event_registration (participant_email, event_id, type, status)" +
                " VALUES (?,?,?,?);";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, registration.getParticipantEmail());
            st.setString(2, registration.getEventId());
            st.setString(3, registration.getType().name());
            st.setString(4, RegistrationStatusEnum.PENDIENTE.name());

            int result = st.executeUpdate();
            if (result == 0) {
                throw new NotRowsAffectedException("No se pudo registrar la inscripción del participante: "
                        + registration.getParticipantEmail() + " en el evento: " + registration.getEventId());
            }
        }
    }

    @Override
    public List<Registration> findAll() throws SQLException, NotFoundException {
        String query = "SELECT * FROM participant_event_registration";
        List<Registration> registrations = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query);
             ResultSet result = st.executeQuery()) {

            while (result.next()) {
                Registration registration = new Registration();
                registration.setParticipantEmail(result.getString("participant_email"));
                registration.setEventId(result.getString("event_id"));
                registration.setType(RegistrationTypeEnum.valueOf(result.getString("type")));
                registration.setStatus(RegistrationStatusEnum.valueOf(result.getString("status")));

                // Extracción de datos de pago
                Payment payment = new Payment();
                String posiblePaymentMethod = result.getString("payment_method");
                payment.setMethod(posiblePaymentMethod == null ? null : PaymentMethodEnum.valueOf(posiblePaymentMethod));
                payment.setAmount(result.getDouble("payment_amount"));
                if (posiblePaymentMethod == null) {
                    registration.setPayment(Optional.empty());
                } else {
                    registration.setPayment(Optional.of(payment));
                }

                registrations.add(registration);
            }
        }
        if (registrations.isEmpty()) {
            throw new NotFoundException("No se hallaron inscripciones registradas");
        }
        return registrations;
    }

    @Override
    public List<Registration> findByAttributes(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        String query = "SELECT * FROM participant_event_registration";
        List<Registration> registrations = new LinkedList<>();
        FilterDTO filterDTO = processFilters(filters, query);

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(filterDTO.getQueryWithFilters())) {
            for (int i = 0; i < filterDTO.getValues().length; i++) {
                st.setObject(i + 1, filterDTO.getValues()[i]);
            }

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Registration registration = new Registration();
                    registration.setParticipantEmail(result.getString("participant_email"));
                    registration.setEventId(result.getString("event_id"));
                    registration.setType(RegistrationTypeEnum.valueOf(result.getString("type")));
                    registration.setStatus(RegistrationStatusEnum.valueOf(result.getString("status")));

                    // Extracción de datos de pago
                    Payment payment = new Payment();
                    String posiblePaymentMethod = result.getString("payment_method");
                    payment.setMethod(posiblePaymentMethod == null ? null : PaymentMethodEnum.valueOf(posiblePaymentMethod));
                    payment.setAmount(result.getDouble("payment_amount"));
                    if (posiblePaymentMethod == null) {
                        registration.setPayment(Optional.empty());
                    } else {
                        registration.setPayment(Optional.of(payment));
                    }

                    registrations.add(registration);
                }
            }
        }
        if (registrations.isEmpty()) {
            throw new NotFoundException("No se hallaron inscripciones en base al filtro");
        }
        return registrations;
    }

    public int getRegistrationsQuantity(String eventId) throws SQLException {
        String query = "SELECT COUNT(*) FROM participant_event_registration WHERE event_id = ?";
        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, eventId);

            ResultSet result = st.executeQuery();
            if (result.next()) {
                return result.getInt("c");
            }
            return 0;
        }
    }
}
