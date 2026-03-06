package finance.com.MyFinance.com.auth;

import finance.com.MyFinance.com.auth.dto.AuthResponse;
import finance.com.MyFinance.com.auth.dto.LoginRequest;
import finance.com.MyFinance.com.auth.dto.RegisterRequest;
import finance.com.MyFinance.com.config.JwtService;
import finance.com.MyFinance.com.user.User;
import finance.com.MyFinance.com.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        System.out.println("🔥 REGISTER HIT");
        String email = req.email().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(req.password()));

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        String email = req.email().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.password())
        );

        String token = jwtService.generateToken(email);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}