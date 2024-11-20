package com.pranav.banking.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User toUser(UserRegisterRequest request) {
        return User.builder()
                .username(request.username())
                .password(encoder.encode(request.password()))
                .pin(request.pin())
                .email(request.email())
                .balance(BigDecimal.ZERO)
                .build();
    }

    public UserProfileResponseDTO toUserProfileResponseDTO(User user) {
        return new UserProfileResponseDTO(user.getUsername(), user.getPassword(), user.getPin(), user.getEmail(), user.getBalance());
    }

    public String updateUser(User user, UserProfileUpdateRequestDTO request) {
        try {
            if (request.email() != null && !request.email().isBlank()) {
                user.setEmail(request.email());
            }
            if (request.pin() != null) {
                user.setPin(request.pin());
            }
            if (request.password() != null && !request.password().isBlank()) {
                user.setPassword(encoder.encode(request.password()));
            }
            // Add more fields as needed

            return "Profile updated successfully!";
        } catch (Exception e) {
            // Log the exception (optional)
            System.err.println("Error updating user: " + e.getMessage());
            return "Failed to update profile.";
        }
    }
}
