package com.pranav.banking.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        Long id,
        String transactionType,
        BigDecimal amount,
        LocalDateTime timestamp,
        String description,
        Long sender,
        Long recipient
) {
}
