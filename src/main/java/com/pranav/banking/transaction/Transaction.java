package com.pranav.banking.transaction;

import com.pranav.banking.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String transactionType; // DEBIT, CREDIT, TRANSFER

    private BigDecimal amount;

    private LocalDateTime timestamp;

    private String description;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient; // For transfers
}
