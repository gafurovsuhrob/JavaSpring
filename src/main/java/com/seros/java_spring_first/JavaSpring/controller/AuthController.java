package com.seros.java_spring_first.JavaSpring.controller;


import com.seros.java_spring_first.JavaSpring.dto.*;
import com.seros.java_spring_first.JavaSpring.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.seros.java_spring_first.JavaSpring.model.AuthProvider;
import com.seros.java_spring_first.JavaSpring.model.Role;
import com.seros.java_spring_first.JavaSpring.model.User;
import com.seros.java_spring_first.JavaSpring.service.UserService;
import com.seros.java_spring_first.JavaSpring.utils.Constants;
import com.seros.java_spring_first.JavaSpring.utils.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.BASE_URL + Constants.AUTH)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping(Constants.LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(loginRequest.getUsername());

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        UserResponse userResponse = userService.convertToUserResponse(user);

        LoginResponse loginResponse = LoginResponse.builder()
                .user(userResponse)
                .token(token)
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(Constants.SIGN_UP)
    public ResponseEntity<?> signUp(@RequestBody @Valid UserRequest signUpRequest) {
        UserResponse userResponse = userService.signUpUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping(Constants.SUCCESS)
    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String providerId = null;
        String name = null;
        String username = null;
        AuthProvider provider = null;
        String password = PasswordUtil.generateRandomPassword(12);

        Map<String, Object> attributes = principal.getAttributes();

        if (isGoogle(attributes)) {
            providerId = (String) attributes.get("sub");
            name = (String) attributes.get("name");
            username = (String) attributes.get("email");
            provider = AuthProvider.GOOGLE;
        } else if (isGitHub(attributes)) {
            providerId = String.valueOf(attributes.get("id"));
            name = (String) attributes.get("name");
            username = (String) attributes.get("login");
            provider = AuthProvider.GITHUB;
        }

        User user = userService.checkByProviderId(providerId);
        if (user == null) {
            String encodedPassword = passwordEncoder.encode(password);

            UserRequest newUser = UserRequest.builder()
                    .name(name)
                    .username(username)
                    .password(encodedPassword)
                    .authProvider(provider)
                    .authProviderId(providerId)
                    .roles(Set.of(2L))
                    .build();
            userService.registerUserSSO(newUser);
            user = userService.checkByProviderId(providerId);
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());

        LoginResponse loginResponse = buildLoginResponse(user, token);
        return ResponseEntity.ok(loginResponse);
    }

    private boolean isGoogle(Map<String, Object> attributes) {
        return attributes.containsKey("sub");
    }

    private boolean isGitHub(Map<String, Object> attributes) {
        return attributes.containsKey("login");
    }

    private LoginResponse buildLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .user(UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .username(user.getUsername())
                        .providerId(user.getAuthProviderId())
                        .provider(user.getAuthProvider().toString())
                        .roles(user.getRoleEntity().stream().map(Role::getId).collect(Collectors.toList()))
                        .build())
                .token(token)
                .build();
    }

    @GetMapping("/failure")
    public ResponseEntity<String> loginFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed login to GitHub.");
    }
}