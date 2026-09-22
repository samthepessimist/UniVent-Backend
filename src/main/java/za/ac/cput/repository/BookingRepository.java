package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.BookingStatusEnum;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    // Find bookings by student
    List<Booking> findByStudent(Student student);

    // Find bookings by event
    List<Booking> findByEvent(Event event);

    // Find bookings by status
    List<Booking> findByStatus(BookingStatusEnum status);

    // Check if booking exists for a student and event
    boolean existsByStudentAndEvent(Student student, Event event);

    // Count bookings for an event
    long countByEvent(Event event);
}