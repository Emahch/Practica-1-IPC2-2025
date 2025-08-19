package com.joca.model.reports;

/**
 *
 * @author joca
 */
public class ActivityReportDTO implements ReportDTO{
    public String activityId;
    public String eventId;
    public String title;
    public String nameSpeaker;
    public String startHour;
    public String maxCapacity;
    public String currentCapacity;

    public ActivityReportDTO() {
    }
    
     @Override
    public String[] getAsRow() {
        return new String[] {
            activityId,
            eventId,
            title,
            nameSpeaker,
            startHour,
            maxCapacity,
            currentCapacity
        };
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNameSpeaker() {
        return nameSpeaker;
    }

    public void setNameSpeaker(String nameSpeaker) {
        this.nameSpeaker = nameSpeaker;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(String currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
}
