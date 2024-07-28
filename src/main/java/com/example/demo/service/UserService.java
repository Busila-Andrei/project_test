package com.example.demo.service;

import com.example.demo.exception.EmailFailureException;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.auth.requests.LoginRequest;
import com.example.demo.auth.requests.RegisterRequest;
import com.example.demo.exception.UserNotVerifiedException;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public User registerUser(RegisterRequest registerRequest) throws UserAlreadyExistException, EmailFailureException {
        if (userRepository.findByEmailIgnoreCase(registerRequest.getEmail()).isPresent()
        || userRepository.findByUsernameIgnoreCase(registerRequest.getLastname() + " " + registerRequest.getFirstname()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        User user = new User();
        user.setUsername(registerRequest.getLastname() + " " + registerRequest.getFirstname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerRequest.getPassword()));
        user = userRepository.save(user);
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        verificationTokenRepository.save(verificationToken);
        return user;
    }

    private VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }


    public String loginUser(LoginRequest loginRequest) throws EmailFailureException, UserNotVerifiedException {
        Optional<User> opUser = userRepository.findByEmailIgnoreCase(loginRequest.getEmail());
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                if (user.getEmailVerification()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.get(0).getCreatedTimestamp().before(
                                    new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }


    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);
        System.out.println(opToken.isPresent());
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            System.out.println(opToken.get().getUser());
            User user = verificationToken.getUser();
            if (!user.getEmailVerification()) {
                user.setEmailVerification(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }


}
