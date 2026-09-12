package za.ac.cput.univentbackend.serviceTest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Event;
import za.ac.cput.repository.EventRepository;
import za.ac.cput.service.EventService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventService service;

    @Test
    public void testCreateEvent() {

        Event event = new Event.Builder()
                .setEventId("evt-1")
                .setName("Tech Talk")
                .setDescription("Java Workshop")
                .setDateTime(LocalDateTime.of(2026, 8, 15, 10, 0))
                .setMaxAttendees(100)
                .build();

        when(repository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Event result = service.create(event);

        assertNotNull(result);
        assertEquals("Tech Talk", result.getName());
        assertEquals("Java Workshop", result.getDescription());
        verify(repository).save(any(Event.class));
    }

    @Test
    public void testReadEvent() {

        Event event = new Event.Builder()
                .setEventId("evt-1")
                .setName("Tech Talk")
                .setDescription("Java Workshop")
                .build();

        when(repository.findById("evt-1")).thenReturn(Optional.of(event));

        Event result = service.read("evt-1");

        assertNotNull(result);
        assertEquals("Tech Talk", result.getName());
    }

    @Test
    public void testUpdateEvent() {

        when(repository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Event updated = new Event.Builder()
                .setEventId("evt-1")
                .setName("Updated Event")
                .setDescription("Updated Description")
                .build();

        Event result = service.update(updated);

        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        verify(repository).save(any(Event.class));
    }

    @Test
    public void testDeleteEvent() {

        doNothing().when(repository).deleteById("evt-1");

        service.delete("evt-1");

        verify(repository).deleteById("evt-1");
    }

    @Test
    public void testCreateNullReturnsNull() {

        assertNull(service.create(null));
    }

    @Test
    public void testReadEventReturnsNull() {

        when(repository.findById("missing")).thenReturn(Optional.empty());

        Event result = service.read("missing");

        assertNull(result);
    }
}
