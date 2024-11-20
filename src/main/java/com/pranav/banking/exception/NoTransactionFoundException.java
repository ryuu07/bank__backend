package com.pranav.banking.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoTransactionFoundException extends RuntimeException {
    private final String message;
}
