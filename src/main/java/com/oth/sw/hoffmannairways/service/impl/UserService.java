package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.repository.UserRepository;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceIF {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) throws UserException {
        return userRepo.findById(username).orElseThrow(
                () -> new UserException("User not found", username));
    }

    @Override
    public User registerUser(User newUser) throws UserException {
        if (userRepo.findById(newUser.getID()).isPresent()) {
            throw new UserException("User already exists", newUser.getUsername());
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepo.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }

    @Override
    public boolean checkPassword(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }
}
