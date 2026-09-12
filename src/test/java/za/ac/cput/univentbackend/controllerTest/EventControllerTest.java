package za.ac.cput.univentbackend.controllerTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import za.ac.cput.controller.EventController;
import za.ac.cput.domain.Event;
import za.ac.cput.service.EventService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService service;

    @InjectMocks
    private EventController controller;

    @Test
    public void testCreateEvent() {

        Event event = new Event.Builder()
                .setEventId("evt-1")
                .setName("Tech Talk")
                .setDescription("Java Workshop")
                .setDateTime(LocalDateTime.of(2026, 8, 15, 10, 0))
                .setMaxAttendees(100)
                .build();

        when(service.create(event)).thenReturn(event);

        ResponseEntity<Event> response = controller.createEvent(event);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Tech Talk", response.getBody().getName());
        assertEquals("Java Workshop", response.getBody().getDescription());

        verify(service).create(event);
    }

    @Test
    public void testGetEventById() {

        Event event = new Event.Builder()
                .setEventId("evt-1")
                .setName("Tech Talk")
                .setDescription("Java Workshop")
                .build();

        when(service.read("evt-1")).thenReturn(event);

        ResponseEntity<Event> response = controller.getEventById("evt-1");

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Tech Talk", response.getBody().getName());

        verify(service).read("evt-1");
    }

    @Test
    public void testUpdateEvent() {

        Event event = new Event.Builder()
                .setEventId("evt-1")
                .setName("Updated Event")
                .setDescription("Updated Description")
                .build();

        when(service.update(event)).thenReturn(event);

        ResponseEntity<Event> response = controller.updateEvent(event);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Updated Event", response.getBody().getName());
        assertEquals("Updated Description", response.getBody().getDescription());

        verify(service).update(event);
    }

    @Test
    public void testDeleteEvent() {

        ResponseEntity<Void> response = controller.deleteEvent("evt-1");

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());

        verify(service).delete("evt-1");
    }
}
