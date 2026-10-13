package za.ac.cput.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.RoleEnum;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.User;
import za.ac.cput.dtos.AuthResponse;
import za.ac.cput.dtos.LoginRequest;
import za.ac.cput.dtos.RegisterRequest;
import za.ac.cput.factory.OrganizerFactory;
import za.ac.cput.factory.StudentFactory;
import za.ac.cput.repository.OrganizerRepository;
import za.ac.cput.repository.StudentRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.util.JwtUtil;
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final OrganizerRepository organizerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       OrganizerRepository organizerRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.organizerRepository = organizerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public AuthResponse register(RegisterRequest request) {


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("An account with this email already exists");
        }


        String hashedPassword = passwordEncoder.encode(request.getPassword());

        RoleEnum role;
        try {
            role = RoleEnum.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: must be STUDENT or ORGANIZER");
        }


        User savedUser;

        if (role == RoleEnum.STUDENT) {
            Student student = StudentFactory.createStudent(
                    request.getName(),
                    request.getEmail(),
                    hashedPassword,
                    request.getPhoneNumber(),
                    request.getStudentNumber(),
                    request.getFaculty(),
                    request.getDepartment(),
                    request.getYearOfStudy()
            );
            savedUser = studentRepository.save(student);

        } else if (role == RoleEnum.ORGANIZER) {
            Organizer organizer = OrganizerFactory.createOrganizer(
                    request.getName(),
                    request.getEmail(),
                    hashedPassword,
                    request.getPhoneNumber(),
                    request.getOrganizationName(),
                    request.getOrganizationType(),
                    request.getOrganizationEmail()
            );
            savedUser = organizerRepository.save(organizer);

        } else {

            throw new IllegalArgumentException("Admin accounts cannot be self-registered");
        }

        String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getRole().name(),
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }


    public AuthResponse login(LoginRequest request) {


        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));


        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }


        // Administrators disable accounts by flagging User.disabled; honour that
        // flag here so a disabled user can never obtain a fresh token. Existing
        // tokens are also rejected by JwtAuthenticationFilter.
        if (user.isDisabled()) {
            throw new IllegalArgumentException("This account has been disabled. Please contact an administrator.");
        }


        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}
