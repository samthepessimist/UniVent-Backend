package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Event;
import za.ac.cput.service.EventService;

/**
 * Read-only event endpoints.
 *
 * <p>Event creation and updates were removed from this controller on purpose:
 * they accepted an organizer from the request body without any ownership check,
 * allowing an organizer to bypass the administrator approval workflow. All
 * organizer writes now go through
 * {@code /api/organizers/{organizerId}/events}, which derives the organizer from
 * the authenticated JWT and forces new events to {@code PENDING_APPROVAL}.</p>
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer id) {
        Event event = eventService.read(id);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

