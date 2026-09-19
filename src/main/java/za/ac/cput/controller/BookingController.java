package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.BookingStatusEnum;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Student;
import za.ac.cput.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Register a student for an event
    @PostMapping("/register")
    public ResponseEntity<?> registerForEvent(@RequestBody RegistrationRequest request) {
        try {
            Booking booking = bookingService.registerForEvent(
                    request.getStudent(),
                    request.getEvent()
            );
            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Cancel a booking
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingId) {
        try {
            Booking booking = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(booking);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get booking by ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable String bookingId) {
        try {
            Booking booking = bookingService.getBookingById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
            return ResponseEntity.ok(booking);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by student
    // TODO: this still needs a StudentService injected to fetch the real Student
    // by ID before calling bookingService.getBookingsByStudent(...) -- passing
    // null will NOT work at runtime, it's left as a placeholder like your original.
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getBookingsByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(bookingService.getBookingsByStudent(null));
    }

    // Get bookings by event
    // TODO: same as above -- needs EventService injected to fetch the real Event.
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getBookingsByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(bookingService.getBookingsByEvent(null));
    }

    // Get bookings by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable BookingStatusEnum status) {
        try {
            List<Booking> bookings = bookingService.getBookingsByStatus(status);
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete booking by ID
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBookingById(@PathVariable String bookingId) {
        try {
            bookingService.deleteBookingById(bookingId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Check if student has booked event
    // TODO: needs StudentService + EventService injected to resolve the real
    // Student and Event before calling hasStudentBookedEvent(...).
    @GetMapping("/exists")
    public ResponseEntity<?> hasStudentBookedEvent(
            @RequestParam String studentId,
            @RequestParam String eventId) {
        return ResponseEntity.ok(bookingService.hasStudentBookedEvent(null, null));
    }

    // Get booking count for event
    // TODO: needs EventService injected to resolve the real Event.
    @GetMapping("/count/event/{eventId}")
    public ResponseEntity<?> getBookingCountForEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(bookingService.getBookingCountForEvent(null));
    }

    // Inner class for registration request
    public static class RegistrationRequest {
        private Student student;
        private Event event;

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }
    }
}