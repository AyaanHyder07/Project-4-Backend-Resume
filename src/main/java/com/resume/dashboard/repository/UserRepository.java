package com.resume.dashboard.repository;

import com.resume.dashboard.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    

}
