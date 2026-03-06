package finance.com.MyFinance.com.transaction;

import finance.com.MyFinance.com.transaction.dto.TransactionRequest;
import finance.com.MyFinance.com.transaction.dto.TransactionResponse;
import finance.com.MyFinance.com.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;
    private final UserRepository userRepository;

    public TransactionController(TransactionService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    private Long currentUserId(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow().getId();
    }

    @GetMapping
    public List<TransactionResponse> list(Authentication auth) {
        return service.list(currentUserId(auth));
    }

    @PostMapping
    public TransactionResponse create(Authentication auth,
                                      @Valid @RequestBody TransactionRequest req) {
        return service.create(currentUserId(auth), req);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(Authentication auth,
                                      @PathVariable Long id,
                                      @Valid @RequestBody TransactionRequest req) {
        return service.update(currentUserId(auth), id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication auth, @PathVariable Long id) {
        service.delete(currentUserId(auth), id);
    }
}