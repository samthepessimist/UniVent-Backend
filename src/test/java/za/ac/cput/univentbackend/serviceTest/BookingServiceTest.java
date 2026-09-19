package za.ac.cput.univentbackend.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.BookingStatusEnum;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.Venue;
import za.ac.cput.factory.BookingFactory;
import za.ac.cput.repository.BookingRepository;
import za.ac.cput.service.BookingService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingFactory bookingFactory;

    @InjectMocks
    private BookingService bookingService;

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
    public void shouldRegisterForEventSuccessfully() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();
        Booking booking = createTestBooking();

        when(bookingRepository.existsByStudentAndEvent(student, event)).thenReturn(false);
        when(bookingRepository.countByEvent(event)).thenReturn(0L);
        when(BookingFactory.createBooking(student, event)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // When
        Booking result = bookingService.registerForEvent(student, event);

        // Then
        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(event, result.getEvent());
        assertEquals(BookingStatusEnum.CONFIRMED, result.getStatus());
        verify(bookingRepository).existsByStudentAndEvent(student, event);
        verify(bookingRepository).countByEvent(event);
        verify(bookingFactory);
        BookingFactory.createBooking(student, event);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void shouldThrowExceptionWhenDuplicateBooking() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();

        when(bookingRepository.existsByStudentAndEvent(student, event)).thenReturn(true);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> bookingService.registerForEvent(student, event));

        assertEquals("Student already has a booking for this event", exception.getMessage());
        verify(bookingRepository).existsByStudentAndEvent(student, event);
        verify(bookingRepository, never()).countByEvent(any(Event.class));
        verify(bookingFactory, never());
        BookingFactory.createBooking(any(Student.class), any(Event.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void shouldThrowExceptionWhenEventFull() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();

        when(bookingRepository.existsByStudentAndEvent(student, event)).thenReturn(false);
        when(bookingRepository.countByEvent(event)).thenReturn(100L);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> bookingService.registerForEvent(student, event));

        assertEquals("Event has reached maximum capacity", exception.getMessage());
        verify(bookingRepository).existsByStudentAndEvent(student, event);
        verify(bookingRepository).countByEvent(event);
        verify(bookingFactory, never());
        BookingFactory.createBooking(any(Student.class), any(Event.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void shouldThrowExceptionWhenEventInPast() {
        // Given
        Student student = createTestStudent();
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

        Event pastEvent = new Event.Builder()
                .setName("Past Event")
                .setDescription("Event in the past")
                .setDateTime(LocalDateTime.now().minusDays(1).toString())
                .setMaxAttendees(100)
                .setOrganizer(organizer)
                .setVenue(venue)
                .build();

        when(bookingRepository.existsByStudentAndEvent(student, pastEvent)).thenReturn(false);
        when(bookingRepository.countByEvent(pastEvent)).thenReturn(0L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.registerForEvent(student, pastEvent));

        assertEquals("Cannot register for past events", exception.getMessage());
        verify(bookingRepository).existsByStudentAndEvent(student, pastEvent);
        verify(bookingRepository).countByEvent(pastEvent);
        verify(bookingFactory, never());
        BookingFactory.createBooking(any(Student.class), any(Event.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void shouldCancelBookingSuccessfully() {
        // Given
        String bookingId = "BKG-123456";
        Booking booking = createTestBooking();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // When
        Booking result = bookingService.cancelBooking(bookingId);

        // Then
        assertNotNull(result);
        assertEquals(BookingStatusEnum.CANCELLED, result.getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void shouldThrowExceptionWhenCancellingAlreadyCancelledBooking() {
        // Given
        String bookingId = "BKG-123456";
        Booking booking = createTestBooking();
        booking.cancel(); // Cancel the booking

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> bookingService.cancelBooking(bookingId));

        assertEquals("Booking is already cancelled", exception.getMessage());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void shouldThrowExceptionWhenCancellingNonExistentBooking() {
        // Given
        String bookingId = "NONEXISTENT";

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.cancelBooking(bookingId));

        assertEquals("Booking not found with id: NONEXISTENT", exception.getMessage());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void shouldGetBookingById() {
        // Given
        String bookingId = "BKG-1";
        Booking booking = createTestBooking();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // When
        Optional<Booking> result = bookingService.getBookingById(bookingId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    public void shouldReturnEmptyWhenBookingNotFoundById() {
        // Given
        String bookingId = "BKG-999";

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When
        Optional<Booking> result = bookingService.getBookingById(bookingId);

        // Then
        assertTrue(result.isEmpty());
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    public void shouldGetAllBookings() {
        // Given
        Booking booking1 = createTestBooking();
        Booking booking2 = createTestBooking();
        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(bookingRepository.findAll()).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getAllBookings();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(bookings, result);
        verify(bookingRepository).findAll();
    }

    @Test
    public void shouldGetEmptyListWhenNoBookings() {
        // Given
        when(bookingRepository.findAll()).thenReturn(List.of());

        // When
        List<Booking> result = bookingService.getAllBookings();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository).findAll();
    }

    @Test
    public void shouldGetBookingsByStudent() {
        // Given
        Student student = createTestStudent();
        Booking booking = createTestBooking();
        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.findByStudent(student)).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getBookingsByStudent(student);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookings, result);
        verify(bookingRepository).findByStudent(student);
    }

    @Test
    public void shouldGetBookingsByEvent() {
        // Given
        Event event = createTestEvent();
        Booking booking = createTestBooking();
        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.findByEvent(event)).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getBookingsByEvent(event);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookings, result);
        verify(bookingRepository).findByEvent(event);
    }

    @Test
    public void shouldGetBookingsByStatus() {
        // Given
        BookingStatusEnum status = BookingStatusEnum.CONFIRMED;
        Booking booking = createTestBooking();
        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.findByStatus(status)).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getBookingsByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookings, result);
        verify(bookingRepository).findByStatus(status);
    }

    @Test
    public void shouldDeleteBookingById() {
        // Given
        String bookingId = "BKG-1";

        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        // When
        bookingService.deleteBookingById(bookingId);

        // Then
        verify(bookingRepository).existsById(bookingId);
        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentBookingById() {
        // Given
        String bookingId = "BKG-999";

        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.deleteBookingById(bookingId));

        assertEquals("Booking not found with id: " + bookingId, exception.getMessage());
        verify(bookingRepository).existsById(bookingId);
        verify(bookingRepository, never()).deleteById(anyString());
    }

    @Test
    public void shouldReturnTrueWhenStudentHasBookedEvent() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();

        when(bookingRepository.existsByStudentAndEvent(student, event)).thenReturn(true);

        // When
        boolean result = bookingService.hasStudentBookedEvent(student, event);

        // Then
        assertTrue(result);
        verify(bookingRepository).existsByStudentAndEvent(student, event);
    }

    @Test
    public void shouldReturnFalseWhenStudentHasNotBookedEvent() {
        // Given
        Student student = createTestStudent();
        Event event = createTestEvent();

        when(bookingRepository.existsByStudentAndEvent(student, event)).thenReturn(false);

        // When
        boolean result = bookingService.hasStudentBookedEvent(student, event);

        // Then
        assertFalse(result);
        verify(bookingRepository).existsByStudentAndEvent(student, event);
    }

    @Test
    public void shouldGetBookingCountForEvent() {
        // Given
        Event event = createTestEvent();

        when(bookingRepository.countByEvent(event)).thenReturn(5L);

        // When
        long count = bookingService.getBookingCountForEvent(event);

        // Then
        assertEquals(5L, count);
        verify(bookingRepository).countByEvent(event);
    }

    @Test
    public void shouldReturnZeroWhenNoBookingsForEvent() {
        // Given
        Event event = createTestEvent();

        when(bookingRepository.countByEvent(event)).thenReturn(0L);

        // When
        long count = bookingService.getBookingCountForEvent(event);

        // Then
        assertEquals(0L, count);
        verify(bookingRepository).countByEvent(event);
    }
}