package za.ac.cput.univentbackend.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.RoleEnum;
import za.ac.cput.domain.Student;
import za.ac.cput.dtos.AuthResponse;
import za.ac.cput.dtos.LoginRequest;
import za.ac.cput.dtos.RegisterRequest;
import za.ac.cput.repository.OrganizerRepository;
import za.ac.cput.repository.StudentRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.AuthService;
import za.ac.cput.util.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private OrganizerRepository organizerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest organizerRequest() {
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "name", "Amanda");
        ReflectionTestUtils.setField(request, "email", "amanda@example.com");
        ReflectionTestUtils.setField(request, "password", "secret");
        ReflectionTestUtils.setField(request, "phoneNumber", "0848882617");
        ReflectionTestUtils.setField(request, "role", "ORGANIZER");
        ReflectionTestUtils.setField(request, "organizationName", "Bitdevs");
        ReflectionTestUtils.setField(request, "organizationType", "NGO");
        ReflectionTestUtils.setField(request, "organizationEmail", "bitdevs@example.com");
        return request;
    }

    private LoginRequest loginRequest() {
        LoginRequest request = new LoginRequest();
        ReflectionTestUtils.setField(request, "email", "amanda@example.com");
        ReflectionTestUtils.setField(request, "password", "secret");
        return request;
    }

    @Test
    void registerOrganizerAssignsUserIdAndPersists() {
        when(userRepository.existsByEmail("amanda@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(organizerRepository.save(any(Organizer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtUtil.generateToken(any(Organizer.class))).thenReturn("token");

        AuthResponse response = authService.register(organizerRequest());

        ArgumentCaptor<Organizer> captor = ArgumentCaptor.forClass(Organizer.class);
        verify(organizerRepository).save(captor.capture());
        Organizer saved = captor.getValue();

        // Without a generated id the organizer cannot be persisted (userId is the @Id)
        assertNotNull(saved.getUserId(), "Organizer registration must assign a user id");
        assertFalse(saved.getUserId().isBlank());
        assertEquals(RoleEnum.ORGANIZER, saved.getRole());
        assertEquals(saved.getUserId(), response.getUserId());
        assertEquals("token", response.getToken());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void loginRejectsDisabledUser() {
        Organizer organizer = new Organizer.Builder()
                .setUserId("org-1")
                .setEmail("amanda@example.com")
                .setPasswordHash("hashed")
                .setRole(RoleEnum.ORGANIZER)
                .build();
        organizer.setDisabled(true);

        when(userRepository.findByEmail("amanda@example.com")).thenReturn(Optional.of(organizer));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.login(loginRequest()));

        assertTrue(exception.getMessage().toLowerCase().contains("disabled"));
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void loginIssuesTokenForEnabledUser() {
        Organizer organizer = new Organizer.Builder()
                .setUserId("org-1")
                .setEmail("amanda@example.com")
                .setPasswordHash("hashed")
                .setRole(RoleEnum.ORGANIZER)
                .build();

        when(userRepository.findByEmail("amanda@example.com")).thenReturn(Optional.of(organizer));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken(organizer)).thenReturn("token");

        AuthResponse response = authService.login(loginRequest());

        assertEquals("token", response.getToken());
        assertEquals("org-1", response.getUserId());
        assertEquals("ORGANIZER", response.getRole());
    }
}
