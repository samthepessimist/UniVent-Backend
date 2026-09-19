package za.ac.cput.univentbackend.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.Venue;
import za.ac.cput.repository.BookingRepository;
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
    private EventRepository eventRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private EventService eventService;

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
                .setOrganizer(mock(Organizer.class))
                .setVenue(mock(Venue.class))
                .build();
    }

    @Test
    void create_WithValidEvent_ShouldSaveAndReturnEvent() {
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.create(event);

        assertNotNull(result);
        assertEquals("Tech Talk", result.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void create_WithNullEvent_ShouldReturnNullAndNotTouchRepository() {
        Event result = eventService.create(null);

        assertNull(result);
        verify(eventRepository, never()).save(any());
    }

    @Test
    void read_WithExistingId_ShouldReturnEvent() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

        Event result = eventService.read(EVENT_ID);

        assertNotNull(result);
        assertEquals(event, result);
        verify(eventRepository, times(1)).findById(EVENT_ID);
    }
}
