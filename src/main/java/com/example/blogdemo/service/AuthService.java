package com.example.blogdemo.service;

//Main business logic

import com.example.blogdemo.dto.AuthenticationResponse;
import com.example.blogdemo.dto.LoginRequest;
import com.example.blogdemo.dto.RegisterRequest;
import com.example.blogdemo.exceptions.CustomAuthenticationException;
import com.example.blogdemo.exceptions.DuplicateEntityException;
import com.example.blogdemo.exceptions.SpringRedditException;
import com.example.blogdemo.model.NotificationEmail;
import com.example.blogdemo.model.User;
import com.example.blogdemo.model.VerificationToken;
import com.example.blogdemo.repository.UserRepository;
import com.example.blogdemo.repository.VerificationTokenRepository;
import com.example.blogdemo.service.jwt.UserDetailsServiceImpl;
import com.example.blogdemo.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private  final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

//    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {
        //Creating user object
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreatedDate(Instant.now());
        user.setEnabled(false); //Set to true if user is validated

        //Check for exist username
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateEntityException("This account has been register");
        }

        //Save to db
        userRepository.save(user);

        //Generate verification token
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Activate your Account", user.getEmail(),
                "Click this link to activate your account: \n" +
                "http://localhost:8080/api/v1/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        //Save verification token to db
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) throws SpringRedditException {
        //Query token in the database to verify
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        //Fetch user and enable them
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) throws SpringRedditException {
        //Fetch user
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found name: " + username));
        //Enable and save to db
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
//            Authentication authUser = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            boolean isEnabled = userDetails.isEnabled();
            String hashedPassword = userDetails.getPassword();
            if (!passwordEncoder.matches(loginRequest.getPassword(), hashedPassword)) {
                throw new CustomAuthenticationException("Wrong password");
            }
            if (isEnabled) {
                String username = userDetails.getUsername();
                String jwt = jwtUtil.generateToken(username);

                return new AuthenticationResponse(jwt, username);
            } else {
                throw new DisabledException("User account is not activated");
            }
        } catch (UsernameNotFoundException e) {
            // Handle disabled user account
            throw new UsernameNotFoundException("User not found");
        }
    }
}
