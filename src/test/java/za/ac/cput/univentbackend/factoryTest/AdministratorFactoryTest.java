package za.ac.cput.univentbackend.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Administrator;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.User;
import za.ac.cput.factory.AdministratorFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdministratorFactoryTest {
    @Test
    public void shouldCreateValidAdministrator(){
        List<User> managedUsers = new ArrayList<>();
        List<Event> managedEvents = new ArrayList<>();
        Administrator administrator = AdministratorFactory.createAdministrator("Sihle", "sihle@gmail.com", "123", "0614845522","Super", new ArrayList<>(), new ArrayList<>());
        assertNotNull(administrator);
        assertEquals("Sihle", administrator.getName());
        assertEquals("sihle@gmail.com", administrator.getEmail());
        assertEquals("123", administrator.getPassword());
        assertEquals("0614845522", administrator.getPhoneNumber());
        assertEquals("Super", administrator.getAdminLevel());
        assertNotNull(administrator.getManagedUsers());
        assertNotNull(administrator.getManagedEvents());
    }

    @Test
    public void showExceptionIfNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("", "sihle@gmail.com", "123", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void showExceptionIfNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("   ", "sihle@gmail.com", "123", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void shouldThrowIfEmailNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "", "123", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void shouldThrowIfEmailIsInvalid(){
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihlegmail.com", "123", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void shouldAcceptValidComplexEmail(){
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihle+test+~@gmail.com", "123", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });

    }

    @Test
    public void showExceptionIfPasswordIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihle@gmail.com", "", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void showExceptionIfPasswordIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihle@gmail.com", "   ", "0614845522", "Super", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void shouldThrowIfPhoneNumberIsInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihle@gmail.com", "123", "061", "Super", new ArrayList<>(), new ArrayList<>());
        });
        assertEquals("Invalid phone number", exception.getMessage());
    }

    @Test
    public void showExceptionIfAdminLevelIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihle@gmail.com", "123", "0614845522", "", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void showExceptionIfAdminLevelIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdministratorFactory.createAdministrator("Sihle", "sihle@gmail.com", "123", "0614845522", "     ", new ArrayList<>(), new ArrayList<>());
        });
    }
}
