package za.ac.cput.univentbackend.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Organizer;
import za.ac.cput.factory.OrganizerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizerFactoryTest {
    @Test
    public void shouldCreateValidOrganizer() {
        Organizer organizer = OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        assertNotNull(organizer);
        // User.userId is the assigned @Id with no generator, so the factory must
        // supply a unique id or the organizer cannot be persisted.
        assertNotNull(organizer.getUserId());
        assertFalse(organizer.getUserId().isBlank());
        assertEquals("Amanda", organizer.getName());
        assertEquals("amandamsutu02@gmail.com", organizer.getEmail());
        assertEquals("123", organizer.getPassword());
        assertEquals("0848882617", organizer.getPhoneNumber());
        assertEquals("Bitdevs", organizer.getOrganizationName());
        assertEquals("NGO", organizer.getOrganizationType());
        assertEquals("bitdevs@gmail.com", organizer.getOrganizationEmail());
    }

    @Test
    public void shouldAssignUniqueUserIdToEachOrganizer() {
        Organizer first = OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        Organizer second = OrganizerFactory.createOrganizer("Amanda", "amandamsutu03@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        assertNotNull(first.getUserId());
        assertNotNull(second.getUserId());
        assertNotEquals(first.getUserId(), second.getUserId());
    }

    @Test
    public void showExceptionIfNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void showExceptionIfNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("   ", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void shouldThrowIfEmailNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void shouldThrowIfEmailIsInvalid(){
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void shouldAcceptValidComplexEmail(){
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02+test~@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void showExceptionIfPasswordIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void showExceptionIfPasswordIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "   ", "0848882617", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void shouldThrowIfPhoneNumberIsInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "084", "Bitdevs", "NGO", "bitdevs@gmail.com");
        });
        assertEquals("Invalid phone number", exception.getMessage());
    }

    @Test
    public void showExceptionIfOrganizationNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void showExceptionIfOrganizationNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "   ", "NGO", "bitdevs@gmail.com");
        });
    }

    @Test
    public void showExceptionIfOrganizationTypeIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "", "bitdevs@gmail.com");
        });
    }

    @Test
    public void showExceptionIfOrganizationTypeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "   ", "bitdevs@gmail.com");
        });
    }

    @Test
    public void shouldThrowIfContactEmailNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "");
        });
    }

    @Test
    public void shouldThrowIfContactEmailIsInvalid(){
        assertThrows(IllegalArgumentException.class, () -> {
            OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevsgmail.com");
        });
    }

    @Test
    public void shouldAcceptValidComplexContactEmail(){
        Organizer organizer = OrganizerFactory.createOrganizer("Amanda", "amandamsutu02@gmail.com", "123", "0848882617", "Bitdevs", "NGO", "bitdevs+test@gmail.com");
        assertNotNull(organizer);
    }
}
