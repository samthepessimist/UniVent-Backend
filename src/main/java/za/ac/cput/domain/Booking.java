package za.ac.cput.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String bookingId;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    protected Booking() {}

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.bookingDate = builder.bookingDate;
        this.status = builder.status;
        this.student = builder.student;
        this.event = builder.event;
    }

    // Getters
    public String getBookingId() { return bookingId; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public BookingStatusEnum getStatus() { return status; }
    public Student getStudent() { return student; }
    public Event getEvent() { return event; }

    // Business method
    public void cancel() {
        if (this.status == BookingStatusEnum.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }
        this.status = BookingStatusEnum.CANCELLED;
    }

    @Override
    public int hashCode() {
        return bookingId == null ? 0 : bookingId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Booking other)) return false;
        return bookingId != null && bookingId.equals(other.bookingId);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                '}';
    }

    public static class Builder {
        private String bookingId;
        private LocalDateTime bookingDate;
        private BookingStatusEnum status;
        private Student student;
        private Event event;

        public Builder() {
            this.bookingId = generateBookingId();
            this.bookingDate = LocalDateTime.now();
            this.status = BookingStatusEnum.CONFIRMED;
        }

        public Builder setBookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setBookingDate(LocalDateTime bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public Builder setStatus(BookingStatusEnum status) {
            this.status = status;
            return this;
        }

        public Builder setStudent(Student student) {
            this.student = student;
            return this;
        }

        public Builder setEvent(Event event) {
            this.event = event;
            return this;
        }

        private String generateBookingId() {
            return "BKG-" + UUID.randomUUID();
        }

        public Booking build() {
            validate();
            return new Booking(this);
        }

        private void validate() {
            if (student == null) {
                throw new IllegalArgumentException("Student cannot be null");
            }
            if (event == null) {
                throw new IllegalArgumentException("Event cannot be null");
            }
            if (bookingDate == null) {
                throw new IllegalArgumentException("Booking date cannot be null");
            }
            if (status == null) {
                throw new IllegalArgumentException("Status cannot be null");
            }
        }
    }
}