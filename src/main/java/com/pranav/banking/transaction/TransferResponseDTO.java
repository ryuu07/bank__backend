package com.pranav.banking.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDTO(
        Long transactionId,      // The unique transaction ID
        Long senderId,           // Sender's user ID
        Long recipientId,        // Recipient's user ID
        BigDecimal senderBalance, // Sender's new balance after transfer
        BigDecimal recipientBalance, // Recipient's new balance after transfer
        BigDecimal amount,       // The amount that was transferred
        String description,      // Description of the transaction
        LocalDateTime timestamp
        ){
}
