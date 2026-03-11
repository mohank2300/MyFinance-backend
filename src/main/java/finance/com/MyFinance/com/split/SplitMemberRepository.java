package finance.com.MyFinance.com.split;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SplitMemberRepository extends JpaRepository<SplitMember, Long> {
    List<SplitMember> findBySplitId(Long splitId);
}