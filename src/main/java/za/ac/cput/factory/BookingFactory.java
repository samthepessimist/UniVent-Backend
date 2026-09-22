package za.ac.cput.factory;

import za.ac.cput.domain.Booking;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.BookingStatusEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingFactory {

    public static Booking createBooking(Student student, Event event) {
        return new Booking.Builder()
                .setStudent(student)
                .setEvent(event)
                .setBookingDate(LocalDateTime.now())
                .setStatus(BookingStatusEnum.CONFIRMED)
                .build();
    }

    public static Booking createBookingWithStatus(Student student, Event event, BookingStatusEnum status) {
        return new Booking.Builder()
                .setStudent(student)
                .setEvent(event)
                .setBookingDate(LocalDateTime.now())
                .setStatus(status)
                .build();
    }

    public static Booking createBookingWithId(String bookingId, Student student,
                                              Event event, BookingStatusEnum status,
                                              LocalDateTime bookingDate) {
        return new Booking.Builder()
                .setBookingId(bookingId)
                .setStudent(student)
                .setEvent(event)
                .setBookingDate(bookingDate)
                .setStatus(status)
                .build();
    }
}