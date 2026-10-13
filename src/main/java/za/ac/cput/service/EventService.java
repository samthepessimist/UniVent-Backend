package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.repository.EventRepository;

@Service
public class EventService implements IEventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event create(Event event) {
        if (event == null) {
            return null;
        }
        // Every event must enter the administrator approval workflow. Forcing the
        // status here means no caller can create an already-published event, even
        // if they manage to reach this service directly.
        event.setStatus(EventStatusEnum.PENDING_APPROVAL);
        return eventRepository.save(event);
    }

    @Override
    public Event read(Integer id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(Integer id) {
        eventRepository.deleteById(id);
    }
}

