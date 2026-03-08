package finance.com.MyFinance.com.report;

import finance.com.MyFinance.com.service.EmailService;
import finance.com.MyFinance.com.transaction.Transaction;
import finance.com.MyFinance.com.transaction.TransactionRepository;
import finance.com.MyFinance.com.user.User;
import finance.com.MyFinance.com.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ReportController(TransactionRepository transactionRepository,
                            UserRepository userRepository,
                            EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    record ReportRequest(String email, String period) {}

    @PostMapping("/send")
    public ResponseEntity<?> sendReport(@RequestBody ReportRequest req, Authentication auth) {
        try {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            Long userId = user.getId();

            Instant from = switch (req.period()) {
                case "THIS_MONTH" -> LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
                case "LAST_30"    -> Instant.now().minus(30, ChronoUnit.DAYS);
                default           -> Instant.parse("2000-01-01T00:00:00Z");
            };

            List<Transaction> transactions = transactionRepository
                    .findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(userId, from);

            String periodLabel = switch (req.period()) {
                case "THIS_MONTH" -> "This Month";
                case "LAST_30"    -> "Last 30 Days";
                default           -> "All Time";
            };

            emailService.sendReport(req.email(), user.getFullName(), transactions, periodLabel);
            return ResponseEntity.ok("Report sent to " + req.email());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to send report: " + e.getMessage());
        }
    }
}