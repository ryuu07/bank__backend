package com.pranav.banking.investment;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "investment_options")
public class InvestmentOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Fixed Deposit 1 Year", "Mutual Fund - Growth", "Bond - 5% Interest"

    @Enumerated(EnumType.STRING)
    private InvestmentType investmentType; // FIXED_DEPOSIT, MUTUAL_FUND, BOND

    private BigDecimal minimumAmount; // Minimum amount required to invest

    private BigDecimal interestRate; // Applicable for Fixed Deposits and Bonds

    private Integer durationInDays; // Duration in days for Fixed Deposits and Bonds

}
