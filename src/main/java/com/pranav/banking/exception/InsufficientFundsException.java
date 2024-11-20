package com.pranav.banking.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InsufficientFundsException extends RuntimeException {
    private final String message;
}

