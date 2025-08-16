package com.joca.services;

import com.joca.database.activity.ActivityDB;
import com.joca.database.event.EventDB;
import com.joca.database.registration.PaymentValidationDB;
import com.joca.database.registration.RegistrationDB;
import com.joca.model.activity.Activity;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterTypeEnum;
import com.joca.model.participant.Participant;
import com.joca.model.registration.Registration;
import com.joca.model.registration.RegistrationTypeEnum;

import java.sql.SQLException;
import java.util.List;

public class ActivityService {
    private ActivityDB activityDB;

    public ActivityService(ActivityDB activityDB) {
        this.activityDB = activityDB;
    }

    public void createActivity(Activity activity) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException, NotFoundException {
        if (isKeyInUse(activity.getId())) {
            throw new DuplicatedKeyException("Error al crear la actividad, el código para la actividad : " + activity.getId() + " ya esta en uso");
        }
        validateActivity(activity);
        activityDB.insert(activity);
    }

    public Activity getActivityByID(String activityID) throws SQLException, NotFoundException {
        return activityDB.findByKey(activityID);
    }

    public List<Activity> getAllActivities() throws SQLException, NotFoundException {
        return activityDB.findAll();
    }

    public void updateActivity(Activity activity, String originalActivityID) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException, NotFoundException {
        if (isKeyInUse(activity.getId()) && !activity.getId().equals(originalActivityID)) {
            throw new DuplicatedKeyException("Error al actualizar la actividad, el id : " + activity.getId() + " ya esta en uso");
        }
        validateActivity(activity);
        activityDB.updateByKey(activity, originalActivityID);
    }

    public void deleteActivity(String activityID) throws SQLException, NotRowsAffectedException {
        activityDB.deleteByKey(activityID);
    }

    public boolean isKeyInUse(String activityID) throws SQLException {
        return activityDB.isKeyInUse(activityID);
    }

    public List<Activity> getActivitiesByFilter(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        return activityDB.findByAttributes(filters);
    }

    private void validateActivity(Activity activity) throws SQLException, InvalidRequisitesException, NotFoundException {
        PaymentValidationDB paymentValidationDB = new PaymentValidationDB();
        if (!paymentValidationDB.isValidated(activity.getSpeakerEmail(), activity.getEventID())) {
            throw new InvalidRequisitesException("El participante " + activity.getSpeakerEmail() + " no se encuentra inscrito validamente en el evento " + activity.getEventID());
        }
        RegistrationDB registrationDB = new RegistrationDB();
        List<Registration> registration = registrationDB.findByAttributes(List.of(
                new Filter("event_id", activity.getEventID(), FilterTypeEnum.EQUAL),
                new Filter("participant_email", activity.getSpeakerEmail(), FilterTypeEnum.EQUAL)
        ));
        if (registration.get(0).getType().equals(RegistrationTypeEnum.ASISTENTE)) {
            throw new InvalidRequisitesException("El participante " + activity.getSpeakerEmail() + " no se puede ser encargado de la actividad debido a que es 'Asistente'");
        }
    }
}
