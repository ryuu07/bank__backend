package com.pranav.banking.investment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvestmentResponseDTO(
        Long id,
        String username,
        String investmentOptionName,
        BigDecimal amount,
        LocalDateTime createdAt
){
}
