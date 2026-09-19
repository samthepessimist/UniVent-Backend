package za.ac.cput.service;

import java.util.List;

import za.ac.cput.domain.Event;

public interface IEventService extends IService<Event, String> {
    List<Event> getUpcomingEvents();
    Event cancelEvent(String eventId);
    boolean hasCapacity(String eventId);
}