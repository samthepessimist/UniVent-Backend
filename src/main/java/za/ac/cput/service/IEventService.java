package za.ac.cput.service;

import za.ac.cput.domain.Event;

import java.util.List;

/**Student name: Uyathandwa Ngomana
 * Student number: 231173229
 * Group: 3H
 * Date: 05 July 2026
 * **/

public interface IEventService extends IService<Event, String> {
    List<Event> getUpcomingEvents();

    Event cancelEvent(String eventId, String organizerId);

    boolean hasCapacity(String eventId);
}