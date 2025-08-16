package com.joca;

import com.joca.database.event.EventDB;
import com.joca.model.event.Event;
import com.joca.model.event.EventTypeEnum;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.services.EventService;

import java.sql.SQLException;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EventService eventService = new EventService(new EventDB());
        Event event = new Event();
        event.setId("EVENT_5");
        event.setType(EventTypeEnum.CHARLA);
        event.setDate(LocalDate.of(1900,12,12));
        event.setTitle("EVENTO AC");
        event.setLocation("U");
        event.setMaxCapacity(4);
        event.setPrice(99.45);
        try {
            eventService.updateEvent(event, "EVENT_5");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidRequisitesException | DuplicatedKeyException e) {
            throw new RuntimeException(e);
        } catch (NotRowsAffectedException e) {
            throw new RuntimeException(e);
        }
    }
}