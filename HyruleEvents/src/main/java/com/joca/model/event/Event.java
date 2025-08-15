package com.joca.model.event;

import com.joca.model.DBEntity;
import com.joca.model.exceptions.InvalidRequisitesException;

import java.time.LocalDate;

public class Event extends DBEntity {

    public static final int MAX_LENGTH_ID = 20;
    public static final int MAX_LENGTH_TITLE = 200;
    public static final int MAX_LENGTH_LOCATION = 150;

    private String id;
    private String title;
    private String location;
    private EventTypeEnum type;
    private LocalDate date;
    private int maxCapacity;
    private double price;

    public Event(String id, String title, String location, EventTypeEnum type, LocalDate date, int maxCapacity, double price) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
        this.date = date;
        this.maxCapacity = maxCapacity;
        this.price = price;
    }

    public Event() {
    }

    @Override
    public void validate() throws InvalidRequisitesException {
        validateString(id, MAX_LENGTH_ID, "un código para el evento");
        validateString(title, MAX_LENGTH_TITLE, "un titulo para el evento");
        validateString(location, MAX_LENGTH_LOCATION, "una ubicación para el evento");
        validateNull(type, "un tipo de evento");
        validateNull(date, "una fecha para el evento");
        validateInt(maxCapacity, "una cantidad máxima para el evento", false);
        validateDouble(price, "un precio para el evento", true);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
