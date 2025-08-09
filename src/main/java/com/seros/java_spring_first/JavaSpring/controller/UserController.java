package com.seros.java_spring_first.JavaSpring.controller;

import com.seros.java_spring_first.JavaSpring.dto.*;
import com.seros.java_spring_first.JavaSpring.model.User;
import com.seros.java_spring_first.JavaSpring.service.UserService;
import com.seros.java_spring_first.JavaSpring.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + Constants.USERS)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(Constants.CHECK_EMAIL)
    public ResponseEntity<Boolean> checkUserByUsername(@PathVariable String email) {
        boolean exists = userService.checkEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.saveUser(userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping(Constants.USER_ID)
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(Constants.GET_ALL_USER_DATA)
    public ResponseEntity<User> getAllUserData(@PathVariable Long id) {
        User user = userService.getAllUserDataById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(Constants.USER_RESET_PASSWORD)
    public ResponseEntity<Void> resetPasswordToDefault(@PathVariable Long id) {
        userService.resetPasswordToDefault(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    @PutMapping(Constants.USER_ID)
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest user) {
        UserResponse updatedUser = userService.updateUser(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(Constants.USER_ADD_ROLE)
    public ResponseEntity<UserResponse> updateUserRoles(@PathVariable Long id, @RequestBody IdList roles) {
        UserResponse updatedUser = userService.updateUserRoles(id, roles);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    @PutMapping(Constants.USER_UPDATE_PASSWORD)
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(Constants.USER_ID)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
