package za.ac.cput.univentbackend.controllerTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import za.ac.cput.controller.OrganizerController;
import za.ac.cput.domain.Event;
import za.ac.cput.domain.Organizer;
import za.ac.cput.dtos.EventResponseDTO;
import za.ac.cput.service.OrganizerService;
import za.ac.cput.util.AuthenticatedUser;
import za.ac.cput.util.UnauthorizedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganizerControllerTest {

	@Mock
	private OrganizerService service;

	@InjectMocks
	private OrganizerController controller;

	@Test
	void testCreateOrganizer() {
		Organizer org = new Organizer.Builder()
				.setOrganizationName("UniVent")
				.setOrganizationEmail("info@univent.com")
				.build();

		when(service.create(org)).thenReturn(org);

		ResponseEntity<Organizer> response = controller.createOrganizer(org);

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals("UniVent", response.getBody().getOrganizationName());
		verify(service).create(org);
	}

	@Test
	void testGetOrganizerById() {
		Organizer org = new Organizer.Builder()
				.setOrganizationName("OrgName")
				.setOrganizationEmail("contact@org.com")
				.build();

		when(service.read("1")).thenReturn(org);

		ResponseEntity<Organizer> response = controller.getOrganizerById("1");

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals("OrgName", response.getBody().getOrganizationName());
		verify(service).read("1");
	}

	@Test
	void testUpdateOrganizer() {
		Organizer org = new Organizer.Builder()
				.setOrganizationName("Updated")
				.build();

		when(service.update(org)).thenReturn(org);

		ResponseEntity<Organizer> response = controller.updateOrganizer(org);

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals("Updated", response.getBody().getOrganizationName());
		verify(service).update(org);
	}

	@Test
	void testDeleteOrganizer() {
		ResponseEntity<Void> response = controller.delete("1");

		assertNotNull(response);
		assertEquals(204, response.getStatusCode().value());
		verify(service).delete("1");
	}

	// ---- Ownership / authorization (organizer is taken from the JWT) ----

	private Authentication organizerAuthentication(String organizerId) {
		AuthenticatedUser principal =
				new AuthenticatedUser(organizerId, organizerId + "@univent.com", "ORGANIZER");
		return new UsernamePasswordAuthenticationToken(
				principal, null, List.of(new SimpleGrantedAuthority("ROLE_ORGANIZER")));
	}

	@Test
	void testCreateEventUsesAuthenticatedOrganizerId() {
		Organizer organizer = new Organizer.Builder().setUserId("org-1").build();
		Event event = new Event.Builder().setName("Tech Talk").build();

		when(service.read("org-1")).thenReturn(organizer);
		when(service.createEvent(any(Event.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		ResponseEntity<EventResponseDTO> response =
				controller.createEvent("org-1", event, organizerAuthentication("org-1"));

		assertNotNull(response.getBody());
		assertSame(organizer, event.getOrganizer());
		verify(service).createEvent(event);
	}

	@Test
	void testCreateEventRejectsSubstitutedOrganizerId() {
		Event event = new Event.Builder().setName("Tech Talk").build();

		assertThrows(UnauthorizedException.class,
				() -> controller.createEvent("org-2", event, organizerAuthentication("org-1")));

		verifyNoInteractions(service);
	}

	@Test
	void testCreateEventWithoutAuthenticationIsRejected() {
		Event event = new Event.Builder().setName("Tech Talk").build();

		assertThrows(UnauthorizedException.class, () -> controller.createEvent("org-1", event, null));

		verifyNoInteractions(service);
	}

	@Test
	void testCancelEventUsesAuthenticatedOrganizerId() {
		Organizer organizer = new Organizer.Builder().setUserId("org-1").build();
		Event event = new Event.Builder().setName("Tech Talk").build();
		event.setOrganizer(organizer);

		when(service.cancelEvent(7, "org-1")).thenReturn(event);

		ResponseEntity<EventResponseDTO> response =
				controller.cancelEvent("org-1", 7, organizerAuthentication("org-1"));

		assertNotNull(response.getBody());
		verify(service).cancelEvent(7, "org-1");
	}

	@Test
	void testGetTotalRegistrationsRejectsSubstitutedOrganizerId() {
		assertThrows(UnauthorizedException.class,
				() -> controller.getTotalRegistrations("org-2", 5, organizerAuthentication("org-1")));

		verifyNoInteractions(service);
	}
}
