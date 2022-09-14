package com.alvis.springbootsource.model;

import com.alvis.springbootsource.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserModel extends AuditModel {
    private String uid;

    private String name;
    private String email;
    // private UserType type;
    private LocalDateTime createdAt;
    private LocalDateTime  updatedAt;
    public UserModel(User user) {
        super(user);
        uid = user.getUid();
        name = user.getName();
        email = user.getEmail();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
    }
}
