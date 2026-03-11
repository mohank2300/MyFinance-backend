package finance.com.MyFinance.com.split;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SplitRepository extends JpaRepository<Split, Long> {
    List<Split> findByCreatedByUserId(Long userId);
}