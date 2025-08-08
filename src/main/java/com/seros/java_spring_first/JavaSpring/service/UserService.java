package com.seros.java_spring_first.JavaSpring.service;

import com.seros.java_spring_first.JavaSpring.dto.*;
import com.seros.java_spring_first.JavaSpring.exceptions.PasswordException;
import com.seros.java_spring_first.JavaSpring.exceptions.RoleException;
import com.seros.java_spring_first.JavaSpring.exceptions.UserNotFoundException;
import com.seros.java_spring_first.JavaSpring.exceptions.WeakPasswordException;
import com.seros.java_spring_first.JavaSpring.model.AuthProvider;
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

    public User checkUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User checkByProviderId(String providerId) {
        return userRepository.findByAuthProviderId(providerId).orElse(null);
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
        validateUserRequest(userRequest);

        User user = createUserFromRequest(userRequest);
        userRepository.save(user);

        return convertToUserResponse(user);
    }

    @Transactional
    public void registerUserSSO(UserRequest signUpRequest) {
        validateUserRequest(signUpRequest);

        User user = createUserFromRequest(signUpRequest);
        userRepository.save(user);

        convertToUserResponse(user);
    }

    @Transactional
    public UserResponse signUpUser(UserRequest signInRequest) {
        UserRequest userRequest = mapSignUpToUserRequest(signInRequest);
        validateUserRequest(userRequest);

        User user = createUserFromRequest(userRequest);
        userRepository.save(user);

        return convertToUserResponse(user);
    }

    @Transactional
    public UserResponse2 signUpUserMobile(UserRequest2 signInRequest) {
        UserRequest2 userRequest = mapSignUpToUserRequest2(signInRequest);

        User user = createUserFromRequest2(userRequest);
        userRepository.save(user);

        return convertToUserResponse2(user);
    }

    private void validateUserRequest(UserRequest userRequest) {
        if (userRequest.getAuthProvider() == AuthProvider.LOCAL &&
                (userRequest.getPassword() == null || userRequest.getPassword().isEmpty())) {
            throw new PasswordException("Password is required for local registration");
        }

        if (userRequest.getRoles().size() > 5) {
            throw new RoleException("You can assign at most 5 roles");
        }
    }

    private User createUserFromRequest(UserRequest userRequest) {
        List<Role> roles = convertIdsToRoles(new ArrayList<>(userRequest.getRoles()));

        return User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .roleEntity(roles)
                .authProvider(userRequest.getAuthProvider())
                .authProviderId(userRequest.getAuthProviderId())
                .build();
    }

    private UserRequest mapSignUpToUserRequest(UserRequest signUpRequest) {
        return UserRequest.builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .authProvider(AuthProvider.LOCAL)
                .authProviderId("local")
                .roles(Set.of(2L))
                .build();
    }

    private User createUserFromRequest2(UserRequest2 userRequest) {
        List<Role> roles = convertIdsToRoles(new ArrayList<>(Set.of(2L)));

        return User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phone(userRequest.getPhone())
                .photoUrl(userRequest.getPhotoUrl())
                .email(userRequest.getEmail())
                .dateOfBirth(userRequest.getDateOfBirth())
                .roleEntity(roles)
                .authProvider((AuthProvider.LOCAL))
                .authProviderId("local")
                .build();
    }

    private UserRequest2 mapSignUpToUserRequest2(UserRequest2 signUpRequest) {
        return UserRequest2.builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .phone(signUpRequest.getPhone())
                .photoUrl(signUpRequest.getPhotoUrl())
                .email(signUpRequest.getEmail())
                .dateOfBirth(signUpRequest.getDateOfBirth())
                .build();
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

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordException("Old password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordException("New password and confirmation password do not match");
        }

        if (request.getNewPassword().length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters long");
        }

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

    public UserResponse convertToUserResponse(User user) {
        String authProviderId = user.getAuthProviderId() != null ? user.getAuthProviderId() : "local";

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .provider(user.getAuthProvider().name())
                .providerId(authProviderId)
                .roles(user.getRoleEntity().stream()
                        .map(Role::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    public UserResponse2 convertToUserResponse2(User user) {

        return UserResponse2.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .photoURL(user.getPhotoUrl())
                .build();
    }

    public UserRequest convertToUserRequest(User user) {
        Set<Long> roles = user.getRoleEntity().stream().map(Role::getId).collect(Collectors.toSet());

        return UserRequest.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .authProvider(user.getAuthProvider())
                .authProviderId(user.getAuthProviderId())
                .roles(roles)
                .build();
    }

    public List<Role> convertIdsToRoles(List<Long> roleIds) {
        return roleIds.stream()
                .map(id -> {
                    Role role = roleService.readById(id);
                    if (role == null) {
                        throw new RoleException("Role with id " + id + " not found");
                    }
                    return role;
                })
                .collect(Collectors.toList());
    }
}
