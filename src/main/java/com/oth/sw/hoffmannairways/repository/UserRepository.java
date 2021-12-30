package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
