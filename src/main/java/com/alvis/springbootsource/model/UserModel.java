package com.alvis.springbootsource.model;

import com.alvis.springbootsource.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {
    private String uid;

    private String name;
    private String email;
    // private UserType type;

    public UserModel(User user) {
        uid = user.getUid();
        name = user.getName();
        email = user.getEmail();
    }
}
