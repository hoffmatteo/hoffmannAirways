package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.repository.UserRepository;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import org.hibernate.service.spi.ServiceException;
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
    public User getUserByUsername(String username) {
        return userRepo.findById(username).orElseThrow(
                () -> new ServiceException("User with email " + username + " not found")
        );
    }

    @Override
    public User registerUser(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepo.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username " + username + " not found")
        );
    }
}
