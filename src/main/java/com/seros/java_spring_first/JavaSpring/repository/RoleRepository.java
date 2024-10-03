package com.seros.java_spring_first.JavaSpring.repository;


import com.seros.java_spring_first.JavaSpring.model.Role;
import com.seros.java_spring_first.JavaSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<User> findByRole(String role);
}
