package za.ac.cput.service;

import za.ac.cput.domain.Booking;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Organizer;

import java.util.List;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * IOrganizerService.java
 * Date: 05 July 2026
 **/

public interface IOrganizerService extends IService<Organizer, String> {
    Organizer findOrganizer(String organizerNumber);


    Event createEvent(Event event);


    Event updateEvent(String organizerId, Event event);


    Event cancelEvent(Integer eventId, String organizerId);


    List<Event> viewMyEvents(String organizerId);


    int getTotalRegistrations(Integer eventId, String organizerId);

    List<Booking> getEventRegistrations(Integer eventId, String organizerId);
}