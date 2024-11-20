package com.pranav.banking.transaction;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreditRequestDTO(
//        @NotNull
//        Long userId,
        @NotNull
        Integer pin,
        @NotNull
        BigDecimal amount,
        String description
){
}
