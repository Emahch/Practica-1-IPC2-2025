package com.joca.model.event;

import java.time.LocalDate;

public class Event {
    private String id;
    private String title;
    private String location;
    private EventTypeEnum type;
    private LocalDate date;
    private int maxCapacity;

    public Event(String id, String title, String location, EventTypeEnum type, LocalDate date, int maxCapacity) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
        this.date = date;
        this.maxCapacity = maxCapacity;
    }

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public EventTypeEnum getType() {
        return type;
    }

    public void setType(EventTypeEnum type) {
        this.type = type;
    }
}
