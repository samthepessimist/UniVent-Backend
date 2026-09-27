package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Administrator;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.User;
import za.ac.cput.dtos.EventResponseDTO;
import za.ac.cput.dtos.PlatformStatsDTO;
import za.ac.cput.dtos.UserResponseDTO;
import za.ac.cput.service.AdministratorService;

import java.util.List;
import java.util.stream.Collectors;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * AdministratorFactory.java
 * Date: 05 July 2026
 * **/

@RestController
@RequestMapping("/api/admin")
public class AdministratorController {
    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService){
        this.administratorService = administratorService;
    }

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<Event> events = administratorService.monitorEvents();
        List<EventResponseDTO> eventDTOs = events.stream()
                .map(event -> new EventResponseDTO(
                        event.getEventId(),
                        event.getName(),
                        event.getDescription(),
                        event.getDateTime(),
                        event.getMaxAttendees(),
                        event.getStatus()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @PatchMapping("/events/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> approveEvent(@PathVariable Integer id) {
        Event event = administratorService.approveEvent(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        EventResponseDTO eventDTO = new EventResponseDTO(
                event.getEventId(),
                event.getName(),
                event.getDescription(),
                event.getDateTime(),
                event.getMaxAttendees(),
                event.getStatus()
        );
        return ResponseEntity.ok(eventDTO);
    }

    @PatchMapping("/events/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> disableEvent(@PathVariable Integer id) {
        Event event = administratorService.disableEvent(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        EventResponseDTO eventDTO = new EventResponseDTO(
                event.getEventId(),
                event.getName(),
                event.getDescription(),
                event.getDateTime(),
                event.getMaxAttendees(),
                event.getStatus()
        );
        return ResponseEntity.ok(eventDTO);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = administratorService.viewAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRole(),
                        user.isDisabled()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PatchMapping("/users/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> disableUser(@PathVariable String id) {
        User user = administratorService.disableUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO userDTO = new UserResponseDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isDisabled()
        );
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatformStatsDTO> getPlatformStats() {
        PlatformStatsDTO stats = administratorService.getPlatformStats();
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator administrator) {
        Administrator createdAdministrator = administratorService.create(administrator);
        return ResponseEntity.ok(createdAdministrator);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable String id) {
        Administrator administrator = administratorService.read(id);
        return ResponseEntity.ok(administrator);
    }

    @PutMapping()
    public ResponseEntity<Administrator> updateAdministrator(@RequestBody Administrator administrator) {
        Administrator updatedAdministrator = administratorService.update(administrator);
        return ResponseEntity.ok(updatedAdministrator);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        administratorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
