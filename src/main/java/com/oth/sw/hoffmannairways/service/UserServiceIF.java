package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceIF extends UserDetailsService {
    User getUserByUsername(String username);
    User registerUser(User newUser);

}
