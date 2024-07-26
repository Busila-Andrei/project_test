package com.example.demo.service;

import com.example.demo.UserAlreadyExistException;
import com.example.demo.auth.model.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest registerRequest) throws UserAlreadyExistException {
        if (userRepository.findByEmailIgnoreCase(registerRequest.getEmail()).isPresent()
        || userRepository.findByUsernameIgnoreCase(registerRequest.getLastname() + " " + registerRequest.getFirstname()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        User user = new User();
        user.setUsername(registerRequest.getLastname() + " " + registerRequest.getFirstname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return userRepository.save(user);
    }


}
