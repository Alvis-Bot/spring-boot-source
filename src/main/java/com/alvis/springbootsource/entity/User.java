package com.alvis.springbootsource.entity;

import com.alvis.springbootsource.common.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import javax.annotation.Nullable;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@TypeDefs({ @TypeDef(name = "string-array", typeClass = StringArrayType.class) })
public class User extends AuditEntity {
    @Id
    private String uid;

    // Hai field fcmTokens và fcmTopics không nên bị side effect ảnh hưởng
    // Vì phải luôn đảm bảo subscribe/unsubscribe topics với tokens phù hợp
    // Do đó, mọi sự thay đổi hai field này phải nằm trong UserFcmTopicService
    // Bên ngoài muốn thay đổi thì phải thông qua UserFcmTopicService này


    @Type(type = "string-array")
    @Column(nullable = false, columnDefinition = "text[]")
    private String[] fcmTokens = {};

    @Type(type = "string-array")
    @Column(nullable = false, columnDefinition = "text[]")
    private String[] fcmTopics = {};

    private String name;
    private String email;
    private String phone;

    private Role role;



}
