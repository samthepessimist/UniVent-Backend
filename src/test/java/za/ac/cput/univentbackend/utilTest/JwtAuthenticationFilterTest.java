package za.ac.cput.univentbackend.utilTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.RoleEnum;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.util.AuthenticatedUser;
import za.ac.cput.util.JwtAuthenticationFilter;
import za.ac.cput.util.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtUtil, userRepository);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private MockHttpServletRequest requestWithToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        return request;
    }

    @Test
    void disabledUserTokenIsNotAuthenticated() throws Exception {
        Organizer disabled = new Organizer.Builder()
                .setUserId("org-1")
                .setEmail("amanda@example.com")
                .setRole(RoleEnum.ORGANIZER)
                .build();
        disabled.setDisabled(true);

        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.extractUserId("token")).thenReturn("org-1");
        when(userRepository.findById("org-1")).thenReturn(Optional.of(disabled));

        filter.doFilter(requestWithToken(), new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "A disabled user's existing token must not authenticate");
    }

    @Test
    void unknownUserTokenIsNotAuthenticated() throws Exception {
        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.extractUserId("token")).thenReturn("missing");
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        filter.doFilter(requestWithToken(), new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void enabledUserTokenSetsAuthenticatedOrganizerPrincipal() throws Exception {
        Organizer organizer = new Organizer.Builder()
                .setUserId("org-1")
                .setEmail("amanda@example.com")
                .setRole(RoleEnum.ORGANIZER)
                .build();

        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.extractUserId("token")).thenReturn("org-1");
        when(userRepository.findById("org-1")).thenReturn(Optional.of(organizer));

        filter.doFilter(requestWithToken(), new MockHttpServletResponse(), new MockFilterChain());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertInstanceOf(AuthenticatedUser.class, authentication.getPrincipal());
        AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        assertEquals("org-1", principal.userId());
        assertEquals("amanda@example.com", principal.email());
        assertTrue(authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ORGANIZER")));
    }
}
