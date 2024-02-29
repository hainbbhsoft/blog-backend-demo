package com.example.blogdemo.service.jwt;

import com.example.blogdemo.exceptions.SpringRedditException;
import com.example.blogdemo.model.User;
import com.example.blogdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found name: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),user.getEnabled(), true, true, true, new ArrayList<>());
    }
}
