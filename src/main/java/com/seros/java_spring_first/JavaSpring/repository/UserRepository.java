package com.seros.java_spring_first.JavaSpring.repository;

import com.seros.java_spring_first.JavaSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByAuthProviderId(String providerId);
    Optional<Boolean> existsByEmail(String email);
}