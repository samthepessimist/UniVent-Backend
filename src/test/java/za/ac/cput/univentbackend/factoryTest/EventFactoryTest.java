package za.ac.cput.univentbackend.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.Venue;
import za.ac.cput.factory.EventFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EventFactoryTest {

    private final Organizer organizer = mock(Organizer.class);
    private final Venue venue = mock(Venue.class);
    private final LocalDateTime dateTime = LocalDateTime.of(2026, 10, 15, 10, 0);

    @Test
    void createEvent_WithValidDetails_ShouldCreatePendingEvent() {
        Event event = EventFactory.createEvent(
                "Tech Talk",
                "Java Workshop",
                dateTime,
                100,
                "https://example.com/poster.png",
                organizer,
                venue);

        assertNotNull(event);
        assertNotNull(event.getEventId());
        assertEquals("Tech Talk", event.getName());
        assertEquals(dateTime, event.getDateTime());
        assertEquals(EventStatusEnum.PENDING_APPROVAL, event.getStatus());
        assertEquals(organizer, event.getOrganizer());
        assertEquals(venue, event.getVenue());
    }

    @Test
    void createEvent_WithNullDateTime_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "Java Workshop",
                        null,
                        100,
                        organizer,
                        venue));

        assertEquals("Invalid date and time", exception.getMessage());
    }
}
