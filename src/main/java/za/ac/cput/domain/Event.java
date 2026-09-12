package za.ac.cput.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**Student name: Uyathandwa Ngomana
 * Student number: 231173229
 * Group: 3H
 * Date: 05 July 2026
 * **/

@Entity
@Table(name = "events")
public class Event {

    @Id
    private String eventId;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private int maxAttendees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatusEnum status = EventStatusEnum.PENDING_APPROVAL;

    private String posterUrl;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    protected Event() {}

    private Event(Builder builder) {
        this.eventId = builder.eventId;
        this.name = builder.name;
        this.description = builder.description;
        this.dateTime = builder.dateTime;
        this.maxAttendees = builder.maxAttendees;
        this.status = builder.status;
        this.posterUrl = builder.posterUrl;
        this.createdAt = builder.createdAt;
        this.organizer = builder.organizer;
        this.venue = builder.venue;
    }

    public String getEventId() {
        return eventId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public int getMaxAttendees() {
        return maxAttendees;
    }
    public EventStatusEnum getStatus() {
        return status;
    }
    public String getPosterUrl() {
        return posterUrl;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Organizer getOrganizer() {
        return organizer;
    }
    public Venue getVenue() {
        return venue;
    }

    public boolean isAvailable() {
        return this.status == EventStatusEnum.APPROVED;
    }

    public void setStatus(EventStatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", maxAttendees=" + maxAttendees +
                ", status=" + status +
                ", posterUrl='" + posterUrl + '\'' +
                ", createdAt=" + createdAt +
                ", organizer=" + organizer +
                ", venue=" + venue + '}';
    }

    public static class Builder {
        private String eventId;
        private String name;
        private String description;
        private LocalDateTime dateTime;
        private int maxAttendees;
        private EventStatusEnum status = EventStatusEnum.PENDING_APPROVAL;
        private String posterUrl;
        private LocalDateTime createdAt;
        private Organizer organizer;
        private Venue venue;

        public Builder setEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }
        public Builder setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }
        public Builder setMaxAttendees(int maxAttendees) {
            this.maxAttendees = maxAttendees;
            return this;
        }
        public Builder setStatus(EventStatusEnum status) {
            this.status = status;
            return this;
        }
        public Builder setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }
        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder setOrganizer(Organizer organizer) {
            this.organizer = organizer;
            return this;
        }
        public Builder setVenue(Venue venue) {
            this.venue = venue;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}