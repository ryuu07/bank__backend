package com.pranav.banking.user;

public record UserLoginRequestDTO(
        String username,
        String password
) {
}
