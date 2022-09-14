package com.alvis.springbootsource.entity;

import com.alvis.springbootsource.common.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.Nullable;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends AuditEntity {
    @Id
    private String uid;

    // Hai field fcmTokens và fcmTopics không nên bị side effect ảnh hưởng
    // Vì phải luôn đảm bảo subscribe/unsubscribe topics với tokens phù hợp
    // Do đó, mọi sự thay đổi hai field này phải nằm trong UserFcmTopicService
    // Bên ngoài muốn thay đổi thì phải thông qua UserFcmTopicService này


    @Transient
    private String[] fcmTokens = {};

    @Transient
    private String[] fcmTopics = {};

    private String name;
    private String email;
    private String phone;

    private Role role;



}
