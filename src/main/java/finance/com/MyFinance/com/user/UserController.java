package finance.com.MyFinance.com.user;

import finance.com.MyFinance.com.config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // GET /api/user/me — get current user profile
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        ));
    }

    // PUT /api/user/me — update name and/or email
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(
            Authentication auth,
            @RequestBody UpdateProfileRequest req) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (req.fullName() != null && !req.fullName().isBlank()) {
            user.setFullName(req.fullName());
        }

        if (req.email() != null && !req.email().isBlank() && !req.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.email())) {
                return ResponseEntity.badRequest().build();
            }
            user.setEmail(req.email().toLowerCase());
        }

        userRepository.save(user);

        return ResponseEntity.ok(new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        ));
    }

    // PUT /api/user/password — change password
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            Authentication auth,
            @RequestBody ChangePasswordRequest req) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (!passwordEncoder.matches(req.currentPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated");
    }

    // DTOs
    public record UserProfileResponse(Long id, String fullName, String email, String role) {}
    public record UpdateProfileRequest(String fullName, String email) {}
    public record ChangePasswordRequest(String currentPassword, String newPassword) {}
}