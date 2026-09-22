package za.ac.cput.univentbackend.controllerTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import za.ac.cput.controller.BookingController;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.BookingStatusEnum;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.Venue;
import za.ac.cput.service.BookingService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Student createTestStudent() {
        return new Student.Builder()
                .setStudentNumber("STU001")
                .setName("John")
                .setEmail("john.doe@university.edu")
                .setPhoneNumber("123-456-7890")
                .setDepartment("Computer Science")
                .build();
    }

    private Event createTestEvent() {
        Organizer organizer = new Organizer.Builder()
                .setName("Tech Org")
                .setEmail("tech@org.com")
                .setPhoneNumber("123456789")
                .build();

        Venue venue = new Venue.Builder()
                .setVenueName("Main Hall")
                .setAddress("123 Main St")
                .setCapacity(Integer.parseInt("500"))
                .build();

        return new Event.Builder()
                .setName("Tech Conference 2024")
                .setDescription("Annual Technology Conference")
                .setDateTime(LocalDateTime.now().plusDays(30).toString())
                .setMaxAttendees(100)
                .setOrganizer(organizer)
                .setVenue(venue)
                .build();
    }

    private Booking createTestBooking() {
        return new Booking.Builder()
                .setStudent(createTestStudent())
                .setEvent(createTestEvent())
                .build();
    }

    @Test
    void testRegisterForEvent() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();
        Booking booking = createTestBooking();

        BookingController.RegistrationRequest request = new BookingController.RegistrationRequest();
        request.setStudent(student);
        request.setEvent(event);

        when(bookingService.registerForEvent(student, event)).thenReturn(booking);

        // When
        ResponseEntity<?> response = bookingController.registerForEvent(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCode().value());
        assertInstanceOf(Booking.class, response.getBody());
        Booking responseBooking = (Booking) response.getBody();
        assertEquals(booking.getBookingId(), responseBooking.getBookingId());
        assertEquals(BookingStatusEnum.CONFIRMED, responseBooking.getStatus());
        verify(bookingService).registerForEvent(student, event);
    }

    @Test
    void testRegisterForEvent_WhenEventFull() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();

        BookingController.RegistrationRequest request = new BookingController.RegistrationRequest();
        request.setStudent(student);
        request.setEvent(event);

        when(bookingService.registerForEvent(student, event))
                .thenThrow(new IllegalStateException("Event has reached maximum capacity"));

        // When
        ResponseEntity<?> response = bookingController.registerForEvent(request);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Event has reached maximum capacity", response.getBody());
        verify(bookingService).registerForEvent(student, event);
    }

    @Test
    void testRegisterForEvent_WhenDuplicateBooking() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();

        BookingController.RegistrationRequest request = new BookingController.RegistrationRequest();
        request.setStudent(student);
        request.setEvent(event);

        when(bookingService.registerForEvent(student, event))
                .thenThrow(new IllegalStateException("Student already has a booking for this event"));

        // When
        ResponseEntity<?> response = bookingController.registerForEvent(request);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Student already has a booking for this event", response.getBody());
        verify(bookingService).registerForEvent(student, event);
    }

    @Test
    void testCancelBooking() {
        // Given
        String bookingId = "BKG-123456";
        Booking booking = createTestBooking();
        booking.cancel();

        when(bookingService.cancelBooking(bookingId)).thenReturn(booking);

        // When
        ResponseEntity<?> response = bookingController.cancelBooking(bookingId);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(Booking.class, response.getBody());
        Booking responseBooking = (Booking) response.getBody();
        assertEquals(BookingStatusEnum.CANCELLED, responseBooking.getStatus());
        verify(bookingService).cancelBooking(bookingId);
    }

    @Test
    void testCancelBooking_WhenAlreadyCancelled() {
        // Given
        String bookingId = "BKG-123456";

        when(bookingService.cancelBooking(bookingId))
                .thenThrow(new IllegalStateException("Booking is already cancelled"));

        // When
        ResponseEntity<?> response = bookingController.cancelBooking(bookingId);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Booking is already cancelled", response.getBody());
        verify(bookingService).cancelBooking(bookingId);
    }

    @Test
    void testCancelBooking_WhenBookingNotFound() {
        // Given
        String bookingId = "NONEXISTENT";

        when(bookingService.cancelBooking(bookingId))
                .thenThrow(new IllegalArgumentException("Booking not found with id: " + bookingId));

        // When
        ResponseEntity<?> response = bookingController.cancelBooking(bookingId);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Booking not found with id: " + bookingId, response.getBody());
        verify(bookingService).cancelBooking(bookingId);
    }

    @Test
    void testGetBookingById() {
        // Given
        String bookingId = "BKG-1";
        Booking booking = createTestBooking();

        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(booking));

        // When
        ResponseEntity<?> response = bookingController.getBookingById(bookingId);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(Booking.class, response.getBody());
        Booking responseBooking = (Booking) response.getBody();
        assertEquals(booking.getBookingId(), responseBooking.getBookingId());
        verify(bookingService).getBookingById(bookingId);
    }

    @Test
    void testGetBookingById_WhenNotFound() {
        // Given
        String bookingId = "BKG-999";

        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = bookingController.getBookingById(bookingId);

        // Then
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Booking not found with id: " + bookingId, response.getBody());
        verify(bookingService).getBookingById(bookingId);
    }

    @Test
    void testGetAllBookings() {
        // Given
        Booking booking1 = createTestBooking();
        Booking booking2 = createTestBooking();

        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking1, booking2));

        // When
        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(bookingService).getAllBookings();
    }

    @Test
    void testGetAllBookings_WhenEmpty() {
        // Given
        when(bookingService.getAllBookings()).thenReturn(List.of());

        // When
        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, response.getBody().size());
        verify(bookingService).getAllBookings();
    }

    @Test
    void testGetBookingsByStatus() {
        // Given
        BookingStatusEnum status = BookingStatusEnum.CONFIRMED;
        Booking booking = createTestBooking();

        when(bookingService.getBookingsByStatus(status)).thenReturn(Collections.singletonList(booking));

        // When
        ResponseEntity<?> response = bookingController.getBookingsByStatus(status);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(List.class, response.getBody());
        @SuppressWarnings("unchecked")
        List<Booking> bookings = (List<Booking>) response.getBody();
        assertEquals(1, bookings.size());
        assertEquals(BookingStatusEnum.CONFIRMED, bookings.get(0).getStatus());
        verify(bookingService).getBookingsByStatus(status);
    }

    @Test
    void testDeleteBookingById() {
        // Given
        String bookingId = "BKG-1";
        doNothing().when(bookingService).deleteBookingById(bookingId);

        // When
        ResponseEntity<?> response = bookingController.deleteBookingById(bookingId);

        // Then
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(bookingService).deleteBookingById(bookingId);
    }

    @Test
    void testDeleteBookingById_WhenNotFound() {
        // Given
        String bookingId = "BKG-999";
        doThrow(new IllegalArgumentException("Booking not found with id: " + bookingId))
                .when(bookingService).deleteBookingById(bookingId);

        // When
        ResponseEntity<?> response = bookingController.deleteBookingById(bookingId);

        // Then
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Booking not found with id: " + bookingId, response.getBody());
        verify(bookingService).deleteBookingById(bookingId);
    }

    @Test
    void testHasStudentBookedEvent() {
        // Given
        when(bookingService.hasStudentBookedEvent(any(), any())).thenReturn(true);

        // When
        ResponseEntity<?> response = bookingController.hasStudentBookedEvent("STU001", "EVT001");

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody());
        verify(bookingService).hasStudentBookedEvent(any(), any());
    }

    @Test
    void testHasStudentBookedEvent_WhenFalse() {
        // Given
        when(bookingService.hasStudentBookedEvent(any(), any())).thenReturn(false);

        // When
        ResponseEntity<?> response = bookingController.hasStudentBookedEvent("STU001", "EVT001");

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(false, response.getBody());
        verify(bookingService).hasStudentBookedEvent(any(), any());
    }

    @Test
    void testGetBookingCountForEvent() {
        // Given
        when(bookingService.getBookingCountForEvent(any())).thenReturn(5L);

        // When
        ResponseEntity<?> response = bookingController.getBookingCountForEvent("EVT001");

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(5L, response.getBody());
        verify(bookingService).getBookingCountForEvent(any());
    }

    @Test
    void testGetBookingCountForEvent_WhenZero() {
        // Given
        when(bookingService.getBookingCountForEvent(any())).thenReturn(0L);

        // When
        ResponseEntity<?> response = bookingController.getBookingCountForEvent("EVT001");

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0L, response.getBody());
        verify(bookingService).getBookingCountForEvent(any());
    }
}