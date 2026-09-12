package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Event;
import za.ac.cput.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.create(event);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        Event event = eventService.read(id);
        return ResponseEntity.ok(event);
    }

    @PutMapping
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        Event updatedEvent = eventService.update(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Event> cancelEvent(@PathVariable String id,
                                             @RequestParam String organizerId) {
        Event cancelled = eventService.cancelEvent(id, organizerId);
        return ResponseEntity.ok(cancelled);
    }

    @GetMapping("/{id}/capacity")
    public ResponseEntity<Boolean> hasCapacity(@PathVariable String id) {
        return ResponseEntity.ok(eventService.hasCapacity(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}