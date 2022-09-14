package com.alvis.springbootsource.component;

import com.alvis.springbootsource.common.Role;
import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.stream.Collectors;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.enabled;

@Component
@RequiredArgsConstructor
@Slf4j
public class Helper {
    private final FirebaseAuth firebaseAuth;

    public Optional<String> extractIdTokenFromAuthHeader(String authHeader) {
        final String HEADER_PREFIX = "Bearer ";
        if (!authHeader.startsWith(HEADER_PREFIX)) return Optional.empty();
        return Optional.of(authHeader.substring(HEADER_PREFIX.length()));
    }
    public Optional<String> extractUidFromIdToken(String idToken) {
        try {
            return Optional.of(firebaseAuth.verifyIdToken(idToken).getUid());
        } catch (FirebaseAuthException e) {
            return Optional.empty();
        }
    }


//    public SimpleGrantedAuthority buildAuthorities(Role role) {
//        switch (role){
//            case ROLE_USER -> {
//                return new SimpleGrantedAuthority(Role.ROLE_USER.name());
//            }
//            case ROLE_ADMIN -> {
//                return new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
//            }
//        }
//        throw new  ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
//    }

    public List<? extends GrantedAuthority> buildAuthorities(Role role) {
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    public boolean isValidFcmToken(String fcmToken) {
        return fcmToken != null && !fcmToken.isEmpty();
    }

    public List<String> getValidFcmTokens(List<String> fcmTokens) {
        if (fcmTokens == null) return List.of();
        return fcmTokens.stream()
                .filter(this::isValidFcmToken)
                .collect(Collectors.toList());
    }

}
