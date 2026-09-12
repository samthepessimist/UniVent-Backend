package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.factory.EventFactory;
import za.ac.cput.repository.BookingRepository;
import za.ac.cput.repository.EventRepository;
import za.ac.cput.util.UnauthorizedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService implements IEventService {
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public EventService(EventRepository eventRepository, BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Event create(Event event) {
        if (event == null) {
            return null;
        }
        Event created = EventFactory.createEvent(
                event.getName(),
                event.getDescription(),
                event.getDateTime(),
                event.getMaxAttendees(),
                event.getOrganizer(),
                event.getVenue(),
                event.getPosterUrl()
        );
        return eventRepository.save(created);
    }

    @Override
    public Event read(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(String id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateTimeAfterAndStatus(
                LocalDateTime.now(),
                EventStatusEnum.APPROVED
        );
    }

    @Override
    public Event cancelEvent(String eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return null;
        }
        if (event.getOrganizer() == null
                || event.getOrganizer().getUserId() == null
                || !event.getOrganizer().getUserId().equals(organizerId)) {
            throw new UnauthorizedException("Organizer does not own this event");
        }
        event.setStatus(EventStatusEnum.CANCELLED);
        return eventRepository.save(event);
    }

    @Override
    public boolean hasCapacity(String eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        return bookingRepository.countByEvent(event) < event.getMaxAttendees();
    }
}