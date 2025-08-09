package com.joca.services.event;

import com.joca.database.event.EventDB;
import com.joca.model.event.Event;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EventService {

    private EventDB eventDB;

    public EventService(EventDB eventDB) {
        this.eventDB = eventDB;
    }

    public void createEvent(Event event) throws SQLException, DuplicatedKeyException, NotRowsAffectedException {
        if (isKeyInUse(event.getId())) {
            throw new DuplicatedKeyException("Error al crear el evento, ya existe un evento con el id: " + event.getId());
        }
        eventDB.insert(event);
    }

    public Event getEventByID(String eventID) throws SQLException, NotFoundException {
        return eventDB.findByKey(eventID);
    }

    public List<Event> getAllEvents() throws SQLException, NotFoundException {
        return eventDB.findAll();
    }

    public void updateEvent(Event event, String originalEventID) throws SQLException, DuplicatedKeyException, NotRowsAffectedException {
        if (isKeyInUse(event.getId())) {
            throw new DuplicatedKeyException("Error al actualizar el evento, ya existe un evento con el id: " + event.getId());
        }

        eventDB.updateByKey(event,originalEventID);
    }

    public void deleteEvent(String eventID) throws SQLException, NotRowsAffectedException {
        eventDB.deleteByKey(eventID);
    }

    public boolean isKeyInUse(String eventID) throws SQLException {
        return eventDB.isKeyInUse(eventID);
    }
}
