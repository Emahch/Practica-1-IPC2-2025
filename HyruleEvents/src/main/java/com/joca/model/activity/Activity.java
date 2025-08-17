package com.joca.model.activity;

import com.joca.model.DBEntity;
import com.joca.model.event.Event;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.participant.Participant;

import java.time.LocalTime;

public class Activity extends DBEntity {

    public static final int MAX_LENGTH_ID = 20;
    public static final int MAX_LENGTH_TITLE = 200;

    private String id;
    private String eventID;
    private String title;
    private String speakerEmail;
    private ActivityTypeEnum type;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxCapacity;

    public Activity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeakerEmail() {
        return speakerEmail;
    }

    public void setSpeakerEmail(String speakerEmail) {
        this.speakerEmail = speakerEmail;
    }

    public ActivityTypeEnum getType() {
        return type;
    }

    public void setType(ActivityTypeEnum type) {
        this.type = type;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void validate() throws InvalidRequisitesException {
        validateString(id, MAX_LENGTH_ID, "un código para la actividad");
        validateString(eventID, Event.MAX_LENGTH_ID, "un código para el evento");
        validateString(title, MAX_LENGTH_TITLE, "un titulo para la actividad");
        validateString(speakerEmail, Participant.MAX_LENGTH_EMAIL, "un email para el encargado");
        validateNull(type, "un tipo de actividad");
        validateNull(startTime, "una hora de inicio");
        validateNull(endTime, "una hora de fin");
        validateInt(maxCapacity, "una capacidad máxima para la actividad", false);

        if (startTime.isAfter(endTime)) {
            throw new InvalidRequisitesException("La hora de inicio debe estar antes de la hora de fin");
        }
    }

    @Override
    public String[] getAsRow() {
        return new String[]{
            id,
            eventID,
            title,
            type.name().toLowerCase(),
            speakerEmail,
            formatTime(startTime),
            formatTime(endTime),
            String.valueOf(maxCapacity)
        };
    }
}
