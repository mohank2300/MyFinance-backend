package finance.com.MyFinance.com.transaction;

import finance.com.MyFinance.com.transaction.dto.TransactionRequest;
import finance.com.MyFinance.com.transaction.dto.TransactionResponse;
import finance.com.MyFinance.com.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> list(Long userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse create(Long userId, TransactionRequest req) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmountCents(req.amountCents());
        tx.setType(req.type());
        tx.setCategory(req.category());
        tx.setDescription(req.description());
        tx.setCreatedAt(Instant.now());

        return toResponse(transactionRepository.save(tx));
    }

    public TransactionResponse update(Long userId, Long id, TransactionRequest req) {
        Transaction tx = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setAmountCents(req.amountCents());
        tx.setType(req.type());
        tx.setCategory(req.category());
        tx.setDescription(req.description());

        return toResponse(transactionRepository.save(tx));
    }

    public void delete(Long userId, Long id) {
        Transaction tx = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(tx);
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAmountCents(),
                t.getType(),
                t.getCategory(),
                t.getDescription(),
                t.getCreatedAt()
        );
    }
}