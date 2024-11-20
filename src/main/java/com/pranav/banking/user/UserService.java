package com.pranav.banking.user;

import com.pranav.banking.config.JWTService;
import com.pranav.banking.exception.InvalidLoginException;
import com.pranav.banking.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public User register(UserRegisterRequest request) {
        User user = mapper.toUser(request);
        return repository.save(user);
    }

    public String login(UserLoginRequestDTO request) {
        try{
            Authentication authentication = authenticationManager
                    .authenticate( new UsernamePasswordAuthenticationToken(request.username(),request.password()));
            if(authentication.isAuthenticated()){
                return jwtService.generateToken(request.username());
            }
        } catch (Exception e) {
            throw new InvalidLoginException("Invalid Username or Password");
        }
        throw  new InvalidLoginException("Login Failed");
    }

    public UserProfileResponseDTO getProfile(String username) {
        User user = repository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("User was not Found"));
        return mapper.toUserProfileResponseDTO(user);
    }

    public String updateProfile(String username, UserProfileUpdateRequestDTO request) {
        User user = repository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("User was not Found"));
        // Update the user's details using the mapper
        String updateMessage = mapper.updateUser(user, request);

        if (updateMessage.equals("Profile updated successfully!")) {
            // Save the updated user back to the database
            repository.save(user);
        }
        return updateMessage;
    }
}
