package za.ac.cput.factory;

import java.time.LocalDateTime;

import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.Venue;
import za.ac.cput.util.Helper;

/**Student name: Uyathandwa Ngomana
 * Student number: 231173229
 * Group: 3H
 * Date: 05 July 2026
 * **/

public class EventFactory {

    public static Event createEvent(String name, String description, LocalDateTime dateTime,
                                    int maxAttendees, Organizer organizer, Venue venue) {
        return createEvent(name, description, dateTime, maxAttendees, null, organizer, venue);
    }

    public static Event createEvent(String name, String description, LocalDateTime dateTime,
                                    int maxAttendees, String posterUrl, Organizer organizer, Venue venue) {
        if (Helper.isNullOrEmpty(name)) throw new IllegalArgumentException("Event name is required");
        if (Helper.isNullOrEmpty(description)) throw new IllegalArgumentException("Description is required");
        if (dateTime == null) throw new IllegalArgumentException("Invalid date and time");
        if (!Helper.isPositive(maxAttendees)) throw new IllegalArgumentException("Maximum attendees must be greater than 0");
        if (organizer == null) throw new IllegalArgumentException("Organizer is required");
        if (venue == null) throw new IllegalArgumentException("Venue is required");

        return new Event.Builder()
                .setEventId(Helper.generateId())
                .setName(name)
                .setDescription(description)
                .setDateTime(dateTime)
                .setMaxAttendees(maxAttendees)
                .setStatus(EventStatusEnum.PENDING_APPROVAL)
                .setPosterUrl(posterUrl)
                .setCreatedAt(LocalDateTime.now())
                .setOrganizer(organizer)
                .setVenue(venue)
                .build();
    }
}

