package com.supermarket.supermarket_api.security.service;

import com.supermarket.supermarket_api.model.User;
import com.supermarket.supermarket_api.repository.UserRepository;
import com.supermarket.supermarket_api.security.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }
}
