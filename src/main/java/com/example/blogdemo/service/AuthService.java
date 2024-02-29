package com.example.blogdemo.service;

//Main business logic

import com.example.blogdemo.dto.AuthenticationResponse;
import com.example.blogdemo.dto.LoginRequest;
import com.example.blogdemo.dto.RegisterRequest;
import com.example.blogdemo.exceptions.SpringRedditException;
import com.example.blogdemo.model.NotificationEmail;
import com.example.blogdemo.model.User;
import com.example.blogdemo.model.VerificationToken;
import com.example.blogdemo.repository.UserRepository;
import com.example.blogdemo.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {
        //Creating user object
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreatedDate(Instant.now());
        user.setEnabled(false); //Set to true if user is validated

        //Save to db
        userRepository.save(user);

        //Generate verification token
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Activate your Account", user.getEmail(),
                "Click this link to activate your account: \n" +
                "http://localhost:8080/api/auth/accountVerification/" + token));
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
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
//                loginRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtProvider.generateToken(authenticate);
//        return new AuthenticationResponse(token, loginRequest.getUsername());
        return new AuthenticationResponse();
    }
}
