package za.ac.cput.univentbackend.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.BookingStatusEnum;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Student;
import za.ac.cput.factory.BookingFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class BookingFactoryTest {

    private final Student student = mock(Student.class);
    private final Event event = mock(Event.class);

    @Test
    public void shouldCreateValidBooking() {
        // When
        Booking booking = BookingFactory.createBooking(student, event);

        // Then
        assertNotNull(booking);
        assertNotNull(booking.getBookingId());
        assertTrue(booking.getBookingId().startsWith("BKG-"));
        assertEquals(student, booking.getStudent());
        assertEquals(event, booking.getEvent());
        assertEquals(BookingStatusEnum.CONFIRMED, booking.getStatus());
        assertNotNull(booking.getBookingDate());
    }

    @Test
    public void shouldCreateBookingWithStatus() {
        // When
        Booking booking = BookingFactory.createBookingWithStatus(
                student, event, BookingStatusEnum.CONFIRMED
        );

        // Then
        assertNotNull(booking);
        assertNotNull(booking.getBookingId());
        assertEquals(student, booking.getStudent());
        assertEquals(event, booking.getEvent());
        assertEquals(BookingStatusEnum.CONFIRMED, booking.getStatus());
        assertNotNull(booking.getBookingDate());
    }

    @Test
    public void shouldCreateBookingWithStatusCancelled() {
        // When
        Booking booking = BookingFactory.createBookingWithStatus(
                student, event, BookingStatusEnum.CANCELLED
        );

        // Then
        assertNotNull(booking);
        assertEquals(student, booking.getStudent());
        assertEquals(event, booking.getEvent());
        assertEquals(BookingStatusEnum.CANCELLED, booking.getStatus());
        assertNotNull(booking.getBookingDate());
    }

    @Test
    public void shouldCreateBookingWithStatusAttended() {
        // When
        Booking booking = BookingFactory.createBookingWithStatus(
                student, event, BookingStatusEnum.ATTENDED
        );

        // Then
        assertNotNull(booking);
        assertEquals(student, booking.getStudent());
        assertEquals(event, booking.getEvent());
        assertEquals(BookingStatusEnum.ATTENDED, booking.getStatus());
        assertNotNull(booking.getBookingDate());
    }

    @Test
    public void shouldCreateBookingWithGivenId() {
        // Given
        String bookingId = "BKG-TEST-001";
        LocalDateTime bookingDate = LocalDateTime.now().minusDays(1);

        // When
        Booking booking = BookingFactory.createBookingWithId(
                bookingId, student, event, BookingStatusEnum.CONFIRMED, bookingDate
        );

        // Then
        assertNotNull(booking);
        assertEquals(bookingId, booking.getBookingId());
        assertEquals(student, booking.getStudent());
        assertEquals(event, booking.getEvent());
        assertEquals(BookingStatusEnum.CONFIRMED, booking.getStatus());
        assertEquals(bookingDate, booking.getBookingDate());
    }

    @Test
    public void shouldThrowIfStudentIsNull() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(null, event));

        assertEquals("Student cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfEventIsNull() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(student, null));

        assertEquals("Event cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfStudentIsNullWithStatus() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithStatus(null, event, BookingStatusEnum.CONFIRMED));

        assertEquals("Student cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfEventIsNullWithStatus() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithStatus(student, null, BookingStatusEnum.CONFIRMED));

        assertEquals("Event cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfStatusIsNull() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithStatus(student, event, null));

        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfStudentIsNullWithId() {
        // Given
        String bookingId = "BKG-TEST-002";
        LocalDateTime bookingDate = LocalDateTime.now();

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithId(bookingId, null, event,
                        BookingStatusEnum.CONFIRMED, bookingDate));

        assertEquals("Student cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfEventIsNullWithId() {
        // Given
        String bookingId = "BKG-TEST-002";
        LocalDateTime bookingDate = LocalDateTime.now();

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithId(bookingId, student, null,
                        BookingStatusEnum.CONFIRMED, bookingDate));

        assertEquals("Event cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfStatusIsNullWithId() {
        // Given
        String bookingId = "BKG-TEST-002";
        LocalDateTime bookingDate = LocalDateTime.now();

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithId(bookingId, student, event,
                        null, bookingDate));

        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowIfBookingDateIsNull() {
        // Given
        String bookingId = "BKG-TEST-002";

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBookingWithId(bookingId, student, event,
                        BookingStatusEnum.CONFIRMED, null));

        assertEquals("Booking date cannot be null", exception.getMessage());
    }

    @Test
    public void shouldGenerateUniqueBookingIds() {
        // Given
        Booking booking1 = BookingFactory.createBooking(student, event);
        Booking booking2 = BookingFactory.createBooking(student, event);

        // Then
        assertNotNull(booking1.getBookingId());
        assertNotNull(booking2.getBookingId());
        assertNotEquals(booking1.getBookingId(), booking2.getBookingId());
        assertTrue(booking1.getBookingId().startsWith("BKG-"));
        assertTrue(booking2.getBookingId().startsWith("BKG-"));
    }

    @Test
    public void shouldSetCurrentDateTimeForBooking() {
        // Given
        LocalDateTime beforeCreation = LocalDateTime.now();

        // When
        Booking booking = BookingFactory.createBooking(student, event);

        // Then
        assertNotNull(booking.getBookingDate());
        assertTrue(booking.getBookingDate().isAfter(beforeCreation) ||
                booking.getBookingDate().equals(beforeCreation));
    }

    @Test
    public void shouldSetGivenBookingDateForBookingWithId() {
        // Given
        String bookingId = "BKG-TEST-003";
        LocalDateTime bookingDate = LocalDateTime.now().minusDays(2);

        // When
        Booking booking = BookingFactory.createBookingWithId(
                bookingId, student, event, BookingStatusEnum.CONFIRMED, bookingDate
        );

        // Then
        assertNotNull(booking);
        assertEquals(bookingId, booking.getBookingId());
        assertEquals(bookingDate, booking.getBookingDate());
    }

    @Test
    public void shouldCreateBookingWithIdFormat() {
        // When
        Booking booking = BookingFactory.createBooking(student, event);
        String bookingId = booking.getBookingId();

        // Then
        assertNotNull(bookingId);
        assertTrue(bookingId.startsWith("BKG-"));
        assertTrue(bookingId.length() > 10);
    }

    @Test
    public void shouldCreateBookingWithConfirmedStatusByDefault() {
        // When
        Booking booking = BookingFactory.createBooking(student, event);

        // Then
        assertEquals(BookingStatusEnum.CONFIRMED, booking.getStatus());
    }

    @Test
    public void shouldCreateBookingWithCancelledStatus() {
        // When
        Booking booking = BookingFactory.createBookingWithStatus(
                student, event, BookingStatusEnum.CANCELLED
        );

        // Then
        assertEquals(BookingStatusEnum.CANCELLED, booking.getStatus());
    }

    @Test
    public void shouldSetAllFieldsForBooking() {
        // Given
        String bookingId = "BKG-TEST-004";
        LocalDateTime bookingDate = LocalDateTime.now().minusHours(5);

        // When
        Booking booking = BookingFactory.createBookingWithId(
                bookingId, student, event, BookingStatusEnum.CONFIRMED, bookingDate
        );

        // Then
        assertNotNull(booking);
        assertEquals(bookingId, booking.getBookingId());
        assertEquals(student, booking.getStudent());
        assertEquals(event, booking.getEvent());
        assertEquals(BookingStatusEnum.CONFIRMED, booking.getStatus());
        assertEquals(bookingDate, booking.getBookingDate());
    }

    @Test
    public void shouldCreateBookingWithDifferentStudentsAndEvents() {
        // Given
        Student student2 = mock(Student.class);
        Event event2 = mock(Event.class);

        // When
        Booking booking1 = BookingFactory.createBooking(student, event);
        Booking booking2 = BookingFactory.createBooking(student2, event2);

        // Then
        assertNotNull(booking1);
        assertNotNull(booking2);
        assertNotEquals(booking1.getStudent(), booking2.getStudent());
        assertNotEquals(booking1.getEvent(), booking2.getEvent());
        assertNotEquals(booking1.getBookingId(), booking2.getBookingId());
    }
}