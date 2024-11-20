package com.pranav.banking.user;

import com.pranav.banking.investment.Investment;
import com.pranav.banking.transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private Integer pin;
    private String email;
    private BigDecimal balance = BigDecimal.ZERO;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
    @OneToMany(
            mappedBy = "recipient",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> receivedTransactions;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investment> investments;


}