package com.alvis.springbootsource.services;

import com.alvis.springbootsource.common.Role;
import com.alvis.springbootsource.component.Helper;
import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.dto.FcmTokenDto;
import com.alvis.springbootsource.dto.UserUpdateDto;
import com.alvis.springbootsource.entity.User;
import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import com.alvis.springbootsource.repository.UserRepository;
import com.alvis.springbootsource.services.base.FcmTokenService;
import com.alvis.springbootsource.services.base.UserService;
import com.alvis.springbootsource.util.CodeUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private  final Helper helper;
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;
    private final FcmTokenService fcmTokenService;

    @Value("${app.DEV_MODE}")
    private Boolean DEV_MODE;

    @Override
    public User findByUserByUid(String uid) {
        return userRepository.findById(uid).orElse(null);
    }

    @Override
    public User registerAdmin(AdminCreateDto dto) {
        if (DEV_MODE != Boolean.TRUE)
            throw new ApiException(ErrorCode.CANNOT_REGISTER_ADMIN);
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new ApiException(ErrorCode.EMAIL_ALREADY_EXIST);
        var userRecord = helper.getFirebaseUserByEmail(dto.getEmail())
                .orElseGet(() -> {
                    var createRequest = createFirebaseUserRequest(dto);
                    return createNewFirebaseUser(createRequest);
                }
        );
        var user = loadFromAdmin(userRecord.getUid(), dto);
        return userRepository.save(user);
    }

    private UserRecord createNewFirebaseUser(UserRecord.CreateRequest createRequest) {
        try {
            return firebaseAuth.createUser(createRequest);
        } catch (FirebaseAuthException e) {
            log.error(e.getAuthErrorCode().name());
            throw new ApiException(ErrorCode.FIREBASE_ERROR).setLoggingEnabled(true);
        }
    }

    @Override
    public User getUser(String myUid) {
        return userRepository.findById(myUid)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }




    private String getUidFromAuthHeader(String headers) {
        var extractIdToken = helper.extractIdTokenFromAuthHeader(headers).orElseThrow(()->{
            throw new ApiException(ErrorCode.INVALID_ID_TOKEN);
        });
        return helper.extractUidFromIdToken(extractIdToken);
    }


    @Override
    public User loginOrRegister(String headers , FcmTokenDto dto) {
        log.info(dto.getFcmToken());
        var uid =getUidFromAuthHeader(headers);
        var userRecord = getFirebaseUser(uid);
        var user = userRepository.findById(uid).orElseGet(()-> createUserEntity(userRecord ,dto ));

        fcmTokenService.addFcmTokenToUser(user, dto.getFcmToken());

        return userRepository.save(user);
    }


    private User createUserEntity(UserRecord firebaseToken , FcmTokenDto dto) {
        var user = new User();
        user.setUid(firebaseToken.getUid());
        user.setEmail(firebaseToken.getEmail());
        user.setName(firebaseToken.getDisplayName());
        user.setRole(Role.ROLE_USER);
        return user;
    }

    @Override
    public void updateUser(String myUid, UserUpdateDto dto) {
        var user = getUser(myUid);
        CodeUtil.updateRequiredField(dto.getName(), user::setName);
        if (dto.getEmail() != null) {
            if (!user.getRole().equals(Role.ROLE_ADMIN))
                throw new ApiException(ErrorCode.CANNOT_CHANGE_ADMIN_EMAIL);
            user.setEmail(dto.getEmail());
        }
        userRepository.save(user);
    }



    private UserRecord getFirebaseUser(String uid) {
        try {
            return firebaseAuth.getUser(uid);
        } catch (FirebaseAuthException e) {
            throw new ApiException(ErrorCode.FIREBASE_ERROR, e.getMessage()).setLoggingEnabled(true);
        }
    }
    private User loadFromAdmin(String uid, AdminCreateDto dto) {
        var user = new User();
        CodeUtil.updateRequiredField(uid ,user::setUid);
        CodeUtil.updateRequiredField(dto.getEmail() , user::setEmail);
        CodeUtil.updateRequiredField(dto.getName() , user::setName);
        CodeUtil.updateRequiredField(Role.ROLE_ADMIN ,user::setRole);
        return user;
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


}
