package com.seros.java_spring_first.JavaSpring.service;

import com.seros.java_spring_first.JavaSpring.exception.UserNotFoundException;
import com.seros.java_spring_first.JavaSpring.model.Role;
import com.seros.java_spring_first.JavaSpring.model.User;
import com.seros.java_spring_first.JavaSpring.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> readAllRoles() {
        return roleRepository.findAll();
    }

    public Role readById(Long id) {
        return roleRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Role with id " + id + " not found")
        );
    }

    public Role createRole(Role creatingRole) {
        Role role = Role.builder()
                .role(creatingRole.getRole())
                .build();
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, String newRoleName) {
        Role role = readById(id);
        role.setRole(newRoleName);
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new UserNotFoundException("Role with id " + id + " not found");
        }
        roleRepository.deleteById(id);
    }
}
