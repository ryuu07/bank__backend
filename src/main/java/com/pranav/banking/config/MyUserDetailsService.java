package com.pranav.banking.config;

import com.pranav.banking.user.User;
import com.pranav.banking.user.UserPrincipal;
import com.pranav.banking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("User Not Found"));


        return new UserPrincipal(user);
    }
}
