package finance.com.MyFinance.com.split;

import finance.com.MyFinance.com.service.EmailService;
import finance.com.MyFinance.com.user.User;
import finance.com.MyFinance.com.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/splits")
public class SplitController {

    private final SplitRepository splitRepository;
    private final SplitMemberRepository splitMemberRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public SplitController(SplitRepository splitRepository,
                           SplitMemberRepository splitMemberRepository,
                           UserRepository userRepository,
                           EmailService emailService) {
        this.splitRepository = splitRepository;
        this.splitMemberRepository = splitMemberRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> createSplit(@RequestBody SplitRequest request, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        Split split = new Split();
        split.setTransactionId(request.getTransactionId());
        split.setCreatedByUserId(user.getId());
        split.setDescription(request.getDescription());
        split.setTotalAmountCents(request.getTotalAmountCents());
        splitRepository.save(split);

        int perPerson = request.getTotalAmountCents() / (request.getFriends().size() + 1);

        for (SplitRequest.Friend friend : request.getFriends()) {
            SplitMember member = new SplitMember();
            member.setSplit(split);
            member.setFriendName(friend.getName());
            member.setFriendEmail(friend.getEmail());
            member.setAmountCents(perPerson);
            splitMemberRepository.save(member);

            // Send email
            String subject = user.getFullName() + " split an expense with you!";
            String body = "<h2>Hi " + friend.getName() + "!</h2>"
                    + "<p><strong>" + user.getFullName() + "</strong> split <strong>"
                    + request.getDescription() + "</strong> with you.</p>"
                    + "<p>Your share: <strong>$" + String.format("%.2f", perPerson / 100.0) + "</strong></p>"
                    + "<p>Total amount: $" + String.format("%.2f", request.getTotalAmountCents() / 100.0) + "</p>"
                    + "<br><p>Sent via MyFinance App</p>";
            try {
                emailService.sendEmail(friend.getEmail(), subject, body);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok(Map.of("message", "Split created and emails sent!"));
    }

    @GetMapping
    public ResponseEntity<?> getSplits(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        List<Split> splits = splitRepository.findByCreatedByUserId(user.getId());
        return ResponseEntity.ok(splits);
    }

    @PutMapping("/members/{memberId}/paid")
    public ResponseEntity<?> markPaid(@PathVariable Long memberId) {
        SplitMember member = splitMemberRepository.findById(memberId).orElseThrow();
        member.setPaid(true);
        splitMemberRepository.save(member);
        return ResponseEntity.ok(Map.of("message", "Marked as paid!"));
    }
}