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
        if (userRepository.findByEmailIgnoreCase(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        User user = new User();
        user.setUsername(registerRequest.getLastName() + " " + registerRequest.getFirstName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword("1234");
        return userRepository.save(user);
    }


}
