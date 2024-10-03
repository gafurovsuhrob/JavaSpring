package com.seros.java_spring_first.JavaSpring.service;

import com.seros.java_spring_first.JavaSpring.dto.IdList;
import com.seros.java_spring_first.JavaSpring.dto.UpdatePasswordRequest;
import com.seros.java_spring_first.JavaSpring.dto.UserRequest;
import com.seros.java_spring_first.JavaSpring.dto.UserResponse;
import com.seros.java_spring_first.JavaSpring.exceptions.PasswordException;
import com.seros.java_spring_first.JavaSpring.exceptions.RoleNotFoundException;
import com.seros.java_spring_first.JavaSpring.exceptions.UserNotFoundException;
import com.seros.java_spring_first.JavaSpring.exceptions.WeakPasswordException;
import com.seros.java_spring_first.JavaSpring.model.Role;
import com.seros.java_spring_first.JavaSpring.model.User;
import com.seros.java_spring_first.JavaSpring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return convertToUserResponse(user);
    }

    public User getAllUserDataById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Transactional
    public void resetPasswordToDefault(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        String defaultPassword = "password";
        user.setPassword(passwordEncoder.encode(defaultPassword));

        userRepository.save(user);
    }


    public User getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponse saveUser(UserRequest userRequest) {
        if (userRequest.getRoles().size() > 5) {
            throw new IllegalArgumentException("You can assign at most 5 roles");
        }

        Set<Long> roleIds = userRequest.getRoles();
        List<Role> roles = !roleIds.isEmpty() ? convertIdsToRoles(new ArrayList<>(roleIds)) : new ArrayList<>();

        User user = User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roleEntity(roles)
                .build();

        userRepository.save(user);
        return convertToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        userRepository.save(user);
        return convertToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserRoles(Long id, IdList roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        Set<Long> roleIds = roles.getIdList();
        List<Role> rolesList = !roleIds.isEmpty() ? convertIdsToRoles(new ArrayList<>(roleIds)) : new ArrayList<>();
        user.setRoleEntity(rolesList);
        userRepository.save(user);
        return convertToUserResponse(user);
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Проверка старого пароля
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordException("Old password is incorrect");
        }

        // Проверка нового пароля и его подтверждения
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordException("New password and confirmation password do not match");
        }

        // Проверка сложности пароля (например, минимум 8 символов)
        if (request.getNewPassword().length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters long");
        }

        // Обновление пароля
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);

        User userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.debug("User found: {}", userEntity.getUsername());

        return org.springframework.security.core.userdetails.User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .roles(user.getRoleEntity().stream()
                        .map(Role::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    private List<Role> convertIdsToRoles(List<Long> roleIds) {
        return roleIds.stream()
                .map(id -> {
                    Role role = roleService.readById(id);
                    if (role == null) {
                        throw new RoleNotFoundException("Role with id " + id + " not found");
                    }
                    return role;
                })
                .collect(Collectors.toList());
    }
}
