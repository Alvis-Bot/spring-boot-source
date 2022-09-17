package com.alvis.springbootsource.component;

import com.alvis.springbootsource.common.Role;
import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class Helper {
    private final FirebaseAuth firebaseAuth;

    public Optional<String> extractIdTokenFromAuthHeader(String authHeader) {
        final String HEADER_PREFIX = "Bearer ";
        if (!authHeader.startsWith(HEADER_PREFIX)) return Optional.empty();
        return Optional.of(authHeader.substring(HEADER_PREFIX.length()));
    }

    public String extractUidFromIdToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken).getUid();
        } catch (FirebaseAuthException ex) {

            if (ex.getAuthErrorCode().name().equals(ErrorCode.INVALID_ID_TOKEN.name())){
                throw new ApiException(ErrorCode.INVALID_ID_TOKEN);
            }else if(ex.getAuthErrorCode().name().equals(ErrorCode.EXPIRED_ID_TOKEN.name())){
                throw new ApiException(ErrorCode.EXPIRED_ID_TOKEN);
            }
            if(ex.getAuthErrorCode().name().equals("USER_NOT_FOUND")){
                throw  new ApiException(ErrorCode.USER_NOT_FOUND , ex.getMessage());
            };
            throw  new ApiException(ErrorCode.FIREBASE_ERROR , ex.getMessage());
        }
    }


    public FirebaseToken extractUserFromIdToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException ex) {

            if (ex.getAuthErrorCode().name().equals(ErrorCode.INVALID_ID_TOKEN.name())){
                throw new ApiException(ErrorCode.INVALID_ID_TOKEN);
            }else if(ex.getAuthErrorCode().name().equals(ErrorCode.EXPIRED_ID_TOKEN.name())){
                throw new ApiException(ErrorCode.EXPIRED_ID_TOKEN);
            }
            if(ex.getAuthErrorCode().name().equals("USER_NOT_FOUND")){
                throw  new ApiException(ErrorCode.USER_NOT_FOUND , ex.getMessage());
            };
            throw  new ApiException(ErrorCode.FIREBASE_ERROR , ex.getMessage());
        }
    }

    public Optional<UserRecord> getFirebaseUserByEmail(String email) {
        try {
            return Optional.ofNullable(firebaseAuth.getUserByEmail(email));
        } catch (FirebaseAuthException e) {
            return Optional.empty();
        }
    }

    public List<? extends GrantedAuthority> buildAuthorities(Role role) {
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    public boolean isValidFcmTopic(String fcmTopic) {
        return fcmTopic != null && !fcmTopic.isEmpty();
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
