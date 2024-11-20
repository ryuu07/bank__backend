package com.pranav.banking.transaction;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequestDTO(
//        Long senderID,
        @NotNull
        Long recipientId,
        @NotNull
        BigDecimal amount,
        @NotNull
        Integer pin,
        String description
) {

}
