package za.ac.cput.univentbackend.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.controller.EventController;
import za.ac.cput.domain.Event;
import za.ac.cput.service.EventService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event event;
    private static final String EVENT_ID = "event-001";

    @BeforeEach
    void setUp() {
        event = new Event.Builder()
                .setEventId(EVENT_ID)
                .setName("Tech Talk")
                .setDescription("Java Workshop")
                .setDateTime(LocalDateTime.of(2026, 10, 15, 10, 0))
                .setMaxAttendees(100)
                .build();
    }

    @Test
    void createEvent_ShouldReturn200AndServiceResult() {
        when(eventService.create(event)).thenReturn(event);

        ResponseEntity<Event> response = eventController.createEvent(event);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(event, response.getBody());
        verify(eventService, times(1)).create(event);
    }

    @Test
    void getEventById_WhenFound_ShouldReturn200AndServiceResult() {
        when(eventService.read(EVENT_ID)).thenReturn(event);

        ResponseEntity<Event> response = eventController.getEventById(EVENT_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(event, response.getBody());
        verify(eventService, times(1)).read(EVENT_ID);
    }
}
