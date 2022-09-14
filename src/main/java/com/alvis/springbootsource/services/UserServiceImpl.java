package com.alvis.springbootsource.services;

import com.alvis.springbootsource.common.Role;
import com.alvis.springbootsource.component.Helper;
import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.dto.FcmTokenDto;
import com.alvis.springbootsource.entity.User;
import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import com.alvis.springbootsource.repository.UserRepository;
import com.alvis.springbootsource.services.base.UserService;
import com.alvis.springbootsource.util.CodeUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private  final Helper helper;

    private final UserRepository userRepository;

    private final FirebaseAuth firebaseAuth;

    @Value("${app.DEV_MODE}")
    private Boolean MODE_DEV;

    @Override
    public User loginOrRegister(String authHeader, FcmTokenDto fcmToken) {

        return null;
    }


    private User createBaseUser(String uid) {
        User user = new User();
        user.setUid(uid);
        return user;
    }
    @Override
    public User findByUserByUid(String uid) {
        return userRepository.findById(uid).orElse(null);
    }

    @Override
    public User createAdmin(String idToken, AdminCreateDto dto) {
        if (MODE_DEV != Boolean.TRUE)
            throw new ApiException(ErrorCode.CANNOT_REGISTER_ADMIN);
        System.out.println(idToken);
        String uid = helper.extractUidFromIdToken(idToken).orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "Invalid id token"));;
        if (userRepository.existsById(uid))
            throw new ApiException(ErrorCode.USER_ALREADY_EXIST);
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new ApiException(ErrorCode.EMAIL_ALREADY_EXIST);
        UserRecord userRecord = getFirebaseUser(uid);
        User user = createAdminEntity(userRecord.getUid(), dto);
        return userRepository.save(user);
    }

    @Override
    public User getUser(String myUid) {
        return userRepository.findById(myUid)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }


    private UserRecord getFirebaseUser(String uid) {
        try {
            return firebaseAuth.getUser(uid);
        } catch (FirebaseAuthException e) {
            throw new ApiException(ErrorCode.FIREBASE_ERROR, e).setLoggingEnabled(true);
        }
    }
    private User createAdminEntity(String uid, AdminCreateDto dto) {
        var user = new User();
        user.setUid(uid);
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setRole(Role.ROLE_ADMIN);
        return user;
    }


    private UserRecord createNewFirebaseUser(UserRecord.CreateRequest createRequest) {
        try {
            return firebaseAuth.createUser(createRequest);
        } catch (FirebaseAuthException e) {
            log.error(e.getMessage());
            throw  new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private UserRecord.CreateRequest createFirebaseUserRequest(AdminCreateDto dto) {
        var createRequest = new UserRecord.CreateRequest();
        createRequest.setEmail(dto.getEmail());
        createRequest.setPassword(dto.getPassword());
        createRequest.setDisplayName(dto.getName());
        createRequest.setEmailVerified(true);
        createRequest.setDisabled(false);
        return createRequest;
    }

    private Optional<UserRecord> getFirebaseUserByEmail(String email) {
        try {
            return Optional.of(firebaseAuth.getUserByEmail(email));
        } catch (FirebaseAuthException e) {
            return Optional.empty();
        }
    }


    private Optional<String> getUidFromAuthHeader(String authHeader) {
        var idToken = CodeUtil.extractTokenFromAuthHeader(authHeader);
        return helper.extractUidFromIdToken(String.valueOf(idToken));
    }

}
