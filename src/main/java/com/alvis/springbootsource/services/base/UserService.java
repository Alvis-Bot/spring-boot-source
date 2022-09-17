package com.alvis.springbootsource.services.base;

import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.dto.FcmTokenDto;
import com.alvis.springbootsource.dto.UserUpdateDto;
import com.alvis.springbootsource.entity.User;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpHeaders;

public interface UserService {
    User loginOrRegister(String headers , FcmTokenDto dto);

    User findByUserByUid(String uid);

    User registerAdmin(AdminCreateDto dto);

    User getUser(String myUid);

    void updateUser(String myUid, UserUpdateDto dto);
}
