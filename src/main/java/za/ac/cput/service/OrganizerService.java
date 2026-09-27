package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.BookingStatusEnum;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.domain.Organizer;
import za.ac.cput.repository.BookingRepository;
import za.ac.cput.repository.EventRepository;
import za.ac.cput.repository.OrganizerRepository;
import za.ac.cput.util.UnauthorizedException;

import java.util.List;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * OrganizerService.java
 * Date: 05 July 2026
 **/

@Service
public class OrganizerService implements IOrganizerService {
    private final OrganizerRepository repository;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public OrganizerService(OrganizerRepository repository,
                            EventRepository eventRepository,
                            BookingRepository bookingRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Organizer findOrganizer(String organizerNumber) {
        return null;
    }

    @Override
    public Organizer create(Organizer organizer) {
        if (organizer == null) {
            return null;
        }
        return repository.save(organizer);
    }

    @Override
    public Organizer read(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Organizer update(Organizer organizer) {
        if (organizer == null) {
            return null;
        }
        return repository.save(organizer);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }


    @Override
    public Event createEvent(Event event) {
        if (event == null || event.getOrganizer() == null) {
            throw new IllegalArgumentException("Event and its organizer are required");
        }
        // every new event starts in the approval workflow
        event.setStatus(EventStatusEnum.PENDING_APPROVAL);
        return eventRepository.save(event);
    }


    @Override
    public Event updateEvent(String organizerId, Event event) {
        if (event == null) {
            return null;
        }
        Event existing = eventRepository.findById(event.getEventId()).orElse(null);
        if (existing == null) {
            return null;
        }
        verifyOwnership(existing, organizerId);
        // an organizer may never reassign ownership or self-approve via update
        event.setOrganizer(existing.getOrganizer());
        event.setStatus(existing.getStatus());
        return eventRepository.save(event);
    }


    @Override
    public Event cancelEvent(Integer eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return null;
        }
        verifyOwnership(event, organizerId);
        event.setStatus(EventStatusEnum.CANCELLED);
        return eventRepository.save(event);
    }


    @Override
    public List<Event> viewMyEvents(String organizerId) {
        Organizer organizer = repository.findById(organizerId).orElse(null);
        if (organizer == null) {
            return List.of();
        }
        return eventRepository.findByOrganizer(organizer);
    }


    @Override
    public int getTotalRegistrations(Integer eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new UnauthorizedException("Event not found");
        }
        verifyOwnership(event, organizerId);
        return (int) bookingRepository.findByEvent(event).stream()
                .filter(booking -> booking.getStatus() == BookingStatusEnum.CONFIRMED)
                .count();
        // If BookingRepository adds: long countByEventAndStatus(Event e, BookingStatusEnum s)
        // then: return (int) bookingRepository.countByEventAndStatus(event, BookingStatusEnum.CONFIRMED);
    }

    @Override
    public List<Booking> getEventRegistrations(Integer eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new UnauthorizedException("Event not found");
        }
        verifyOwnership(event, organizerId);
        return bookingRepository.findByEvent(event);
    }

    private void verifyOwnership(Event event, String organizerId) {
        // organizerId must come from the authenticated principal (never the URL alone)
        if (organizerId == null
                || event.getOrganizer() == null
                || event.getOrganizer().getUserId() == null
                || !event.getOrganizer().getUserId().equals(organizerId)) {
            throw new UnauthorizedException("Organizer does not own this event");
        }
    }
}