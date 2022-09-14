package com.alvis.springbootsource.services.base;

import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.dto.FcmTokenDto;
import com.alvis.springbootsource.dto.UserCreateDto;
import com.alvis.springbootsource.dto.UserUpdateDto;
import com.alvis.springbootsource.entity.User;

import java.util.Optional;

public interface UserService {
    User loginOrRegister(String authHeader , FcmTokenDto fcmToken);

    User findByUserByUid(String uid);

    User createAdmin(AdminCreateDto dto);

    User getUser(String myUid);

    User createUser(String idToken);

    void updateUser(String myUid, UserUpdateDto dto);
}
