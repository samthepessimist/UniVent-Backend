package za.ac.cput.service;

import za.ac.cput.domain.Booking;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.BookingStatusEnum;
import za.ac.cput.factory.BookingFactory;
import za.ac.cput.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingFactory bookingFactory;

    @Override
    public Booking registerForEvent(Student student, Event event) {
        // Business rule: Check for duplicate booking
        if (hasStudentBookedEvent(student, event)) {
            throw new IllegalStateException("Student already has a booking for this event");
        }

        // Business rule: Check event capacity
        long currentBookings = getBookingCountForEvent(event);
        if (currentBookings >= event.getMaxAttendees()) {
            throw new IllegalStateException("Event has reached maximum capacity");
        }

        // Business rule: Check if event is in the future
        // NOTE: assumes Event.getDateTime() returns a String in ISO-8601 format
        // (e.g. "2026-09-20T14:00:00"), which is what LocalDateTime.parse() expects
        // by default. If your Event stores dates differently, this parse will need
        // a DateTimeFormatter to match -- tell me the actual format and I'll adjust.
        LocalDateTime eventDateTime = LocalDateTime.parse(event.getDateTime());
        if (eventDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot register for past events");
        }

        // Create booking using factory
        Booking booking = BookingFactory.createBooking(student, event);

        // Save booking
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // Business rule: Check if booking can be cancelled
        if (booking.getStatus() == BookingStatusEnum.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // Cancel booking
        booking.cancel();

        // Update repository
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByStudent(Student student) {
        return bookingRepository.findByStudent(student);
    }

    @Override
    public List<Booking> getBookingsByEvent(Event event) {
        return bookingRepository.findByEvent(event);
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatusEnum status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public void deleteBookingById(String bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public boolean hasStudentBookedEvent(Student student, Event event) {
        return bookingRepository.existsByStudentAndEvent(student, event);
    }

    @Override
    public long getBookingCountForEvent(Event event) {
        return bookingRepository.countByEvent(event);
    }
}