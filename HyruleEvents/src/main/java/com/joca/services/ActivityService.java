package com.joca.services;

import com.joca.database.activity.ActivityDB;
import com.joca.database.event.EventDB;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.activity.Activity;

import java.sql.SQLException;
import java.util.List;

public class ActivityService {
    private ActivityDB activityDB;

    public ActivityService(ActivityDB activityDB) {
        this.activityDB = activityDB;
    }

    public void createActivity(Activity activity) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException {
        if (isKeyInUse(activity.getId())) {
            throw new DuplicatedKeyException("Error al crear la actividad, el id : " + activity.getId() + " ya esta en uso");
        }
        EventDB eventDB = new EventDB();
        if (!eventDB.isKeyInUse(activity.getEventID())) {
            throw new InvalidRequisitesException("Error al crear la actividad, el evento con id: " + activity.getEventID() + " no existe");
        }
        //Pendiente de validación de speaker
        activityDB.insert(activity);
    }

    public Activity getActivityByID(String activityID) throws SQLException, NotFoundException {
        return activityDB.findByKey(activityID);
    }

    public List<Activity> getAllActivities() throws SQLException, NotFoundException {
        return activityDB.findAll();
    }

    public void updateActivity(Activity activity, String originalActivityID) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, InvalidRequisitesException {
        if (isKeyInUse(activity.getId())) {
            throw new DuplicatedKeyException("Error al actualizar la actividad, el id : " + activity.getId() + " ya esta en uso");
        }
        EventDB eventDB = new EventDB();
        if (!eventDB.isKeyInUse(activity.getEventID())) {
            throw new InvalidRequisitesException("Error al actualizar la actividad, el evento con id: " + activity.getEventID() + " no existe");
        }
        //Pendiente de validación de speaker
        activityDB.updateByKey(activity,originalActivityID);
    }

    public void deleteActivity(String activityID) throws SQLException, NotRowsAffectedException {
        activityDB.deleteByKey(activityID);
    }

    public boolean isKeyInUse(String activityID) throws SQLException {
        return activityDB.isKeyInUse(activityID);
    }

    public List<Activity> getActivitiesByFilter(List<Filter> filters) throws SQLException, NotFoundException {
        return activityDB.findByAttributes(filters);
    }
}
