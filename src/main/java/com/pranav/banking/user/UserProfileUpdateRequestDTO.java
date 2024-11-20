package com.pranav.banking.user;

public record UserProfileUpdateRequestDTO(
        String password,
        Integer pin,
        String email
) {
}
