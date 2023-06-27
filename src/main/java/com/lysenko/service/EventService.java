package com.lysenko.service;

import com.lysenko.entity.Event;
import com.lysenko.repository.EventRepository;
import com.lysenko.repository.hibernateImpl.EventRepositoryImpl;

import java.util.List;

public class EventService {

    private final EventRepository eventRepository;

    public EventService() {
        this.eventRepository = new EventRepositoryImpl();
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public Event update(Event event, Integer id) {
        return eventRepository.update(id, event);
    }

    public Event findById(Integer id) {
        return eventRepository.findById(Event.class, id);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public void delete(Integer id) {
        eventRepository.delete(Event.class, id);
    }
}
