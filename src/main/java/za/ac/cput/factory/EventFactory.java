package za.ac.cput.factory;

import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.Venue;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class EventFactory {

    public static Event createEvent(String name,
                                    String description,
                                    LocalDateTime dateTime,
                                    int maxAttendees,
                                    String posterUrl,
                                    Organizer organizer,
                                    Venue venue) {

        if (Helper.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (Helper.isNullOrEmpty(description)) {
            throw new IllegalArgumentException("Description is required");
        }
        if (!Helper.isValidDateTime(dateTime)) {
            throw new IllegalArgumentException("Event date and time must be in the future");
        }
        if (!Helper.isPositive(maxAttendees)) {
            throw new IllegalArgumentException("Maximum attendees must be greater than 0");
        }
        if (organizer == null) {
            throw new IllegalArgumentException("Organizer is required");
        }
        if (venue == null) {
            throw new IllegalArgumentException("Venue is required");
        }

        return new Event.Builder()
                .setEventId(Helper.generateId())
                .setName(name)
                .setDescription(description)
                .setDateTime(dateTime)
                .setMaxAttendees(maxAttendees)
                .setPosterUrl(posterUrl)
                .setCreatedAt(LocalDateTime.now())
                .setStatus(EventStatusEnum.PENDING_APPROVAL)
                .setOrganizer(organizer)
                .setVenue(venue)
                .build();
    }
}