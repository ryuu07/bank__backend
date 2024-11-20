package com.pranav.banking.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditResponseDTO(
        Long transactionId,
        Long userId,
        BigDecimal newBalance,
        LocalDateTime timestamp,
        String description
) {
}
