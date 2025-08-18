package com.joca.services;

import com.joca.database.event.EventDB;
import com.joca.database.participant.ParticipantDB;
import com.joca.database.registration.RegistrationDB;
import com.joca.model.event.Event;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterTypeEnum;
import com.joca.model.registration.Registration;

import java.sql.SQLException;
import java.util.List;

public class RegistrationService {

    private RegistrationDB registrationDB;

    public RegistrationService(RegistrationDB registrationDB) {
        this.registrationDB = registrationDB;
    }

    public void createRegistration(Registration registration) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, NotFoundException, InvalidRequisitesException {
        registration.validate();
        if (isKeysInUse(registration.getParticipantEmail(), registration.getEventId())) {
            throw new DuplicatedKeyException("Error al registrar la inscripción, el participante : "
                    + registration.getParticipantEmail() + " ya se encuentra registrado en el evento: " + registration.getEventId());
        }
        if (!isAvailableCapacity(registration.getEventId())) {
            throw new InvalidRequisitesException("El evento ya alcanzó su cupo máximo");
        }
        ParticipantService participantService = new ParticipantService(new ParticipantDB());
        if (!participantService.isKeyInUse(registration.getParticipantEmail())) {
            throw new InvalidRequisitesException("No se encontró un participante con email " + registration.getParticipantEmail());
        }
        registrationDB.insert(registration);
    }

    public Registration getRegistrationByID(String participantEmail, String eventId) throws SQLException, NotFoundException, InvalidRequisitesException {
        Filter filterEmail = new Filter("participant_email", participantEmail, FilterTypeEnum.EQUAL);
        Filter filterEvent = new Filter("event_id", eventId, FilterTypeEnum.EQUAL);
        List<Registration> registrations = registrationDB.findByAttributes(List.of(filterEmail, filterEvent));
        return registrations.get(0);
    }

    public List<Registration> getAllRegistrations() throws SQLException, NotFoundException {
        return registrationDB.findAll();
    }

    public void updateRegistration(Registration registration, String participantEmail, String eventId) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException {
        registration.validate();
        if (isKeysInUse(registration.getParticipantEmail(), registration.getEventId())
                && !isSameKey(registration, participantEmail, eventId)) {
            throw new DuplicatedKeyException("Error al actualizar la inscripción, el participante : "
                    + registration.getParticipantEmail() + " ya se encuentra registrado en el evento: " + registration.getEventId());
        }
        ParticipantService participantService = new ParticipantService(new ParticipantDB());
        if (!participantService.isKeyInUse(registration.getParticipantEmail())) {
            throw new InvalidRequisitesException("No se encontró un participante con email " + registration.getParticipantEmail());
        }
        registrationDB.updateByKeys(registration, participantEmail, eventId);
    }

    public void deleteRegistration(String participantEmail, String eventId) throws SQLException, NotRowsAffectedException {
        registrationDB.deleteByKeys(participantEmail, eventId);
    }

    public boolean isKeysInUse(String participantEmail, String eventId) throws SQLException {
        return registrationDB.isKeysInUse(participantEmail, eventId);
    }

    public List<Registration> getRegistrationsByFilter(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        return registrationDB.findByAttributes(filters);
    }

    private boolean isSameKey(Registration registration, String oldEmail, String oldId) {
        return registration.getParticipantEmail().equals(oldEmail) && registration.getEventId().equals(oldId);
    }

    public boolean isAvailableCapacity(String eventId) throws SQLException, NotFoundException {
        int actualCapacity = registrationDB.getRegistrationsQuantity(eventId);
        EventDB eventDB = new EventDB();
        Event event = eventDB.findByKey(eventId);
        return event.getMaxCapacity() > actualCapacity;
    }
}
