package za.ac.cput.dtos;

import java.time.LocalDateTime;

import za.ac.cput.domain.EventStatusEnum;

public class EventResponseDTO {
    private String eventId;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private int maxAttendees;
    private EventStatusEnum status;

    public EventResponseDTO(String eventId, String name, String description, LocalDateTime dateTime, int maxAttendees, EventStatusEnum status) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.maxAttendees = maxAttendees;
        this.status = status;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public int getMaxAttendees() { return maxAttendees; }
    public void setMaxAttendees(int maxAttendees) { this.maxAttendees = maxAttendees; }
    public EventStatusEnum getStatus() { return status; }
    public void setStatus(EventStatusEnum status) { this.status = status; }
}