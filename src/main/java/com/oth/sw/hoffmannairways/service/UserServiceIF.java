package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServiceIF extends UserDetailsService {
    User getUserByUsername(String username) throws UserException;

    User registerUser(User newUser) throws UserException;

    boolean checkPassword(String password, User user);

    List<User> getAllUsers();

}
