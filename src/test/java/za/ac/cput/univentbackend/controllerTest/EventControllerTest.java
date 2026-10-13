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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService service;

    @InjectMocks
    private EventController controller;

    // Event creation/update were intentionally removed from EventController so
    // that all organizer writes go through /api/organizers/**, where the
    // organizer is derived from the authenticated principal. See OrganizerControllerTest.

    @Test
    public void testGetEventById() {

        Event event = new Event.Builder()
                .setName("Tech Talk")
                .setDescription("Java Workshop")
                .build();

        when(service.read(1)).thenReturn(event);

        ResponseEntity<Event> response = controller.getEventById(1);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Tech Talk", response.getBody().getName());

        verify(service).read(1);
    }

    @Test
    public void testDeleteEvent() {

        ResponseEntity<Void> response = controller.deleteEvent(1);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());

        verify(service).delete(1);
    }
}
