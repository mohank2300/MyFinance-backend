package finance.com.MyFinance.com.split;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "splits")
public class Split {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;
    private Long createdByUserId;
    private String description;
    private int totalAmountCents;
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "split", cascade = CascadeType.ALL)
    private List<SplitMember> members;

    // Getters and Setters
    public Long getId() { return id; }
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getTotalAmountCents() { return totalAmountCents; }
    public void setTotalAmountCents(int totalAmountCents) { this.totalAmountCents = totalAmountCents; }
    public Instant getCreatedAt() { return createdAt; }
    public List<SplitMember> getMembers() { return members; }
    public void setMembers(List<SplitMember> members) { this.members = members; }
}