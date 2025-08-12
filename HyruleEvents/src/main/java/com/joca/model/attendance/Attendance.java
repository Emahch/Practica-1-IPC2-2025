package com.joca.model.attendance;

public class Attendance {
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
}
