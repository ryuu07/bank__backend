package com.pranav.banking.investment;

import java.math.BigDecimal;

public record InvestmentRequestBody(
        Long investmentOptionId,
        BigDecimal amount
) {
}
