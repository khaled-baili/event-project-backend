package com.eventproject.service;

import com.eventproject.model.Event;
import com.eventproject.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired private EventRepo eventRepo;

    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    public Event getEventById(long idEvent) {
        return eventRepo.findEventByIdEvent(idEvent);
    }

    public Event saveEvent(Event event) {
        return eventRepo.save(event);
    }

    public void deleteEvent(Event event) {
        eventRepo.delete(event);
    }

    public void deleteEventById(long id) {
        eventRepo.deleteById(id);
    }

    public void deleteAllEvents() {
        eventRepo.deleteAll();
    }




}
