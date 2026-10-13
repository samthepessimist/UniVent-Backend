package za.ac.cput.univentbackend.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import za.ac.cput.domain.Organizer;
import za.ac.cput.repository.OrganizerRepository;
import za.ac.cput.service.OrganizerService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizerServiceTest {

	@Mock
	private OrganizerRepository repository;

	@InjectMocks
	private OrganizerService service;

	@Test
	void testCreateOrganizer() {
		Organizer org = new Organizer.Builder()
				.setOrganizationName("UniVent")
				.setOrganizationEmail("info@univent.com")
				.build();

		when(repository.save(any(Organizer.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Organizer result = service.create(org);

		assertNotNull(result);
		assertEquals("UniVent", result.getOrganizationName());
		assertEquals("info@univent.com", result.getOrganizationEmail());
		verify(repository).save(any(Organizer.class));
	}

	@Test
	void testReadOrganizer() {
		Organizer org = new Organizer.Builder()
				.setOrganizationName("OrgName")
				.setOrganizationEmail("contact@org.com")
				.build();

		when(repository.findById("1")).thenReturn(Optional.of(org));

		Organizer result = service.read("1");

		assertNotNull(result);
		assertEquals("OrgName", result.getOrganizationName());
	}

	@Test
	void testUpdateOrganizer() {
		when(repository.save(any(Organizer.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Organizer updated = new Organizer.Builder()
				.setOrganizationName("NewName")
				.build();

		Organizer result = service.update(updated);

		assertNotNull(result);
		assertEquals("NewName", result.getOrganizationName());
		verify(repository).save(any(Organizer.class));
	}

	@Test
	void testDeleteOrganizer() {
		doNothing().when(repository).deleteById("1");

		service.delete("1");

		verify(repository).deleteById("1");
	}

	@Test
	void testCreateNullReturnsNull() {
		assertNull(service.create(null));
	}

	@Test
	void testUpdateNullReturnsNull() {
		assertNull(service.update(null));
	}

	@Test
	void testFindOrganizerReturnsNull() {
		// Current service implementation returns null for findOrganizer
		assertNull(service.findOrganizer("123"));
	}
}
