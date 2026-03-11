package finance.com.MyFinance.com.split;

import jakarta.persistence.*;

@Entity
@Table(name = "split_members")
public class SplitMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "split_id")
    private Split split;

    private String friendName;
    private String friendEmail;
    private int amountCents;
    private boolean paid = false;

    // Getters and Setters
    public Long getId() { return id; }
    public Split getSplit() { return split; }
    public void setSplit(Split split) { this.split = split; }
    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }
    public String getFriendEmail() { return friendEmail; }
    public void setFriendEmail(String friendEmail) { this.friendEmail = friendEmail; }
    public int getAmountCents() { return amountCents; }
    public void setAmountCents(int amountCents) { this.amountCents = amountCents; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}