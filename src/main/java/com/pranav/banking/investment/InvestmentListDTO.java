package com.pranav.banking.investment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvestmentListDTO(
        Long id,
        String investmentOptionName,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime end_time,
        BigDecimal payout,
        Long referenceId
) {
}
