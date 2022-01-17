package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
}
