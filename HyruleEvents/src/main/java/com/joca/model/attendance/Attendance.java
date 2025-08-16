package com.joca.model.attendance;

import com.joca.model.DBEntity;
import com.joca.model.activity.Activity;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.participant.Participant;

public class Attendance extends DBEntity {
    private String participantEmail;
    private String activityId;

    public Attendance(String participantEmail, String activityId) {
        this.participantEmail = participantEmail;
        this.activityId = activityId;
    }

    public Attendance() {
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @Override
    public void validate() throws InvalidRequisitesException {
        validateString(participantEmail, Participant.MAX_LENGTH_EMAIL, "un email para el participante");
        validateString(activityId, Activity.MAX_LENGTH_ID, "un código para la actividad");
    }
}
