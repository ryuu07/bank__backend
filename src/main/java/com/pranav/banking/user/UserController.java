package com.pranav.banking.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<User> register (
            @RequestBody UserRegisterRequest request
    ){
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(
            @RequestBody UserLoginRequestDTO request
    ){
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping(path= "/home")
    public ResponseEntity<String> getLoggedInUserDetails(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @GetMapping(path= "/profile")
    public ResponseEntity<UserProfileResponseDTO> getProfileDetails(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(userService.getProfile(userDetails.getUsername()));
    }

    @PutMapping(path = "/profile")
    public ResponseEntity<String> updateProfileDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserProfileUpdateRequestDTO request
    ){
        return ResponseEntity.ok(userService.updateProfile(userDetails.getUsername(), request));
    }
}
