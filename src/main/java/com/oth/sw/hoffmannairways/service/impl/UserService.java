package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.repository.UserRepository;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import com.oth.sw.hoffmannairways.util.logger.LoggerIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserServiceIF {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("DatabaseLogger")
    private LoggerIF databaseLogger;

    @Override
    public User getUserByUsername(String username) throws UserException {

        User user = userRepo.findById(username).orElseThrow(
                () -> new UserException("User not found", username));
        databaseLogger.log("UserService", "Returning user " + user);
        return user;

    }

    @Override
    public User registerUser(User newUser) throws UserException {
        if (userRepo.findById(newUser.getID()).isPresent()) {
            throw new UserException("User already exists", newUser.getUsername());
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepo.save(newUser);
        databaseLogger.log("UserService", "Registering user " + savedUser);
        return savedUser;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        databaseLogger.log("UserService", "Returning user " + user);
        return user;

    }

    @Override
    public boolean checkPassword(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
