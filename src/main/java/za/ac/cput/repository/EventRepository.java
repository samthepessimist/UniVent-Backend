package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.EventStatusEnum;
import za.ac.cput.domain.Organizer;

import java.util.List;

/**Student name: Uyathandwa Ngomana
 * Student number: 231173229
 * Group: 3H
 * Date: 05 July 2026
 * **/

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByOrganizer(Organizer organizer);
    List<Event> findByStatus(EventStatusEnum status);
}
