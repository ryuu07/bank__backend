package com.pranav.banking.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotNull
        Integer pin,
        @Email
        @NotBlank
        String email
) {
}
