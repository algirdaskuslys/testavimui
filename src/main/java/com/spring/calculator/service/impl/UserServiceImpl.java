package com.spring.calculator.service.impl;

import com.spring.calculator.model.User;
import com.spring.calculator.repository.UserRepository;
import com.spring.calculator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("BCryptPasswordEncoder")
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(User.UserRole.ADMIN);

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isEmailTaken(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private User convertEntityToDto(User user) {
        User userDto = new User();
        String[] name = user.getUsername().split(" ");
        userDto.setUsername(name[0]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    @Override
    public User findByIdWithCalculations(int id) {
        return userRepository.findByIdWithCalculations(id);
    }
}
