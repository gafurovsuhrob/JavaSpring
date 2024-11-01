package com.seros.java_spring_first.JavaSpring.oauth;

import com.seros.java_spring_first.JavaSpring.model.AuthProvider;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    @Getter
    private final AuthProvider authProvider;

    public CustomOAuth2User(OAuth2User oAuth2User, AuthProvider authProvider) {
        this.oAuth2User = oAuth2User;
        this.authProvider = authProvider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

}
