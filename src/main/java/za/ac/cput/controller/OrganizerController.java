package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Organizer;
import za.ac.cput.dtos.EventResponseDTO;
import za.ac.cput.service.OrganizerService;
import za.ac.cput.util.AuthenticatedUser;
import za.ac.cput.util.UnauthorizedException;

import java.util.List;
import java.util.stream.Collectors;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * OrganizerController.java
 * Date: 05 July 2026
 **/

@RestController
@RequestMapping("/api/organizers")
public class OrganizerController {
    private final OrganizerService organizerService;

    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }


    @PostMapping("/{organizerId}/events")
    public ResponseEntity<EventResponseDTO> createEvent(@PathVariable String organizerId,
                                                        @RequestBody Event event,
                                                        Authentication authentication) {
        // Never trust the {organizerId} path value on its own: it must match the
        // organizer carried by the authenticated JWT.
        String authenticatedOrganizerId = resolveOrganizerId(organizerId, authentication);
        Organizer organizer = organizerService.read(authenticatedOrganizerId);
        if (organizer == null) {
            return ResponseEntity.notFound().build();
        }
        event.setOrganizer(organizer);
        return ResponseEntity.ok(toEventDTO(organizerService.createEvent(event)));
    }


    @PutMapping("/{organizerId}/events")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable String organizerId,
                                                        @RequestBody Event event,
                                                        Authentication authentication) {
        String authenticatedOrganizerId = resolveOrganizerId(organizerId, authentication);
        Event updated = organizerService.updateEvent(authenticatedOrganizerId, event);
        return updated == null ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(toEventDTO(updated));
    }


    @PatchMapping("/{organizerId}/events/{eventId}/cancel")
    public ResponseEntity<EventResponseDTO> cancelEvent(@PathVariable String organizerId,
                                                        @PathVariable Integer eventId,
                                                        Authentication authentication) {
        String authenticatedOrganizerId = resolveOrganizerId(organizerId, authentication);
        Event event = organizerService.cancelEvent(eventId, authenticatedOrganizerId);
        return event == null ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(toEventDTO(event));
    }


    @GetMapping("/{organizerId}/events")
    public ResponseEntity<List<EventResponseDTO>> viewMyEvents(@PathVariable String organizerId,
                                                               Authentication authentication) {
        String authenticatedOrganizerId = resolveOrganizerId(organizerId, authentication);
        return ResponseEntity.ok(
                organizerService.viewMyEvents(authenticatedOrganizerId).stream()
                        .map(this::toEventDTO)
                        .collect(Collectors.toList()));
    }


    @GetMapping("/{organizerId}/events/{eventId}/registrations/count")
    public ResponseEntity<Integer> getTotalRegistrations(@PathVariable String organizerId,
                                                         @PathVariable Integer eventId,
                                                         Authentication authentication) {
        String authenticatedOrganizerId = resolveOrganizerId(organizerId, authentication);
        return ResponseEntity.ok(organizerService.getTotalRegistrations(eventId, authenticatedOrganizerId));
    }

    // ---- Organizer CRUD ----
    @PostMapping
    public ResponseEntity<Organizer> createOrganizer(@RequestBody Organizer organizer) {
        return ResponseEntity.ok(organizerService.create(organizer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizer> getOrganizerById(@PathVariable String id) {
        Organizer organizer = organizerService.read(id);
        return organizer == null ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(organizer);
    }

    @PutMapping
    public ResponseEntity<Organizer> updateOrganizer(@RequestBody Organizer organizer) {
        return ResponseEntity.ok(organizerService.update(organizer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        organizerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Resolves the organizer id used for event operations from the authenticated
     * principal. The {@code organizerId} supplied in the URL is only accepted when
     * it matches the signed-in organizer, so an organizer can never act on behalf
     * of another organizer by editing the path.
     */
    private String resolveOrganizerId(String organizerId, Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof AuthenticatedUser principal)) {
            throw new UnauthorizedException("Organizer authentication is required");
        }

        String authenticatedOrganizerId = principal.userId();
        if (authenticatedOrganizerId == null || authenticatedOrganizerId.isBlank()) {
            throw new UnauthorizedException("Organizer authentication is required");
        }

        if (organizerId != null && !organizerId.equals(authenticatedOrganizerId)) {
            throw new UnauthorizedException("Organizer may only manage their own events");
        }

        return authenticatedOrganizerId;
    }

    private EventResponseDTO toEventDTO(Event event) {
        return new EventResponseDTO(
                event.getEventId(),
                event.getName(),
                event.getDescription(),
                event.getDateTime(),
                event.getMaxAttendees(),
                event.getStatus()
        );
    }
}