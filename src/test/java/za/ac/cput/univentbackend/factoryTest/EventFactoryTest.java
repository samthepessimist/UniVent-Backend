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
    private final LocalDateTime dateTime = LocalDateTime.of(2026, 8, 15, 10, 0);

    @Test
    public void shouldCreateValidEvent() {

        Event event = EventFactory.createEvent(
                "Tech Talk",
                "Java Workshop",
                dateTime,
                100,
                organizer,
                venue
        );

        assertNotNull(event);
        assertFalse(event.getEventId().isEmpty());
        assertEquals("Tech Talk", event.getName());
        assertEquals("Java Workshop", event.getDescription());
        assertEquals(dateTime, event.getDateTime());
        assertEquals(100, event.getMaxAttendees());
        assertEquals(EventStatusEnum.PENDING_APPROVAL, event.getStatus());
        assertFalse(event.isAvailable());
        assertNotNull(event.getCreatedAt());
        assertEquals(organizer, event.getOrganizer());
        assertEquals(venue, event.getVenue());
    }

    @Test
    public void shouldThrowIfNameIsEmpty() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "",
                        "Java Workshop",
                        dateTime,
                        100,
                        organizer,
                        venue));

        assertEquals("Event name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowIfNameIsBlank() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "   ",
                        "Java Workshop",
                        dateTime,
                        100,
                        organizer,
                        venue));

        assertEquals("Event name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowIfDescriptionIsEmpty() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "",
                        dateTime,
                        100,
                        organizer,
                        venue));

        assertEquals("Description is required", exception.getMessage());
    }

    @Test
    public void shouldThrowIfDescriptionIsBlank() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "   ",
                        dateTime,
                        100,
                        organizer,
                        venue));

        assertEquals("Description is required", exception.getMessage());
    }

    @Test
    public void shouldThrowIfDateTimeIsInvalid() {

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

    @Test
    public void shouldThrowIfMaxAttendeesIsZero() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "Java Workshop",
                        dateTime,
                        0,
                        organizer,
                        venue));

        assertEquals("Maximum attendees must be greater than 0", exception.getMessage());
    }

    @Test
    public void shouldThrowIfMaxAttendeesIsNegative() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "Java Workshop",
                        dateTime,
                        -10,
                        organizer,
                        venue));

        assertEquals("Maximum attendees must be greater than 0", exception.getMessage());
    }

    @Test
    public void shouldThrowIfOrganizerIsNull() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "Java Workshop",
                        dateTime,
                        100,
                        null,
                        venue));

        assertEquals("Organizer is required", exception.getMessage());
    }

    @Test
    public void shouldThrowIfVenueIsNull() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                EventFactory.createEvent(
                        "Tech Talk",
                        "Java Workshop",
                        dateTime,
                        100,
                        organizer,
                        null));

        assertEquals("Venue is required", exception.getMessage());
    }
}
