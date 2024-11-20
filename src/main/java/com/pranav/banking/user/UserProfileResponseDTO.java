package com.pranav.banking.user;

import java.math.BigDecimal;

public record UserProfileResponseDTO(
        String username,
        String password,
        Integer pin,
        String email,
        BigDecimal balance
) {
}
