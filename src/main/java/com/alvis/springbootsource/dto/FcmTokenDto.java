package com.alvis.springbootsource.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Getter
@Setter
public class FcmTokenDto {
    @NotEmpty(message = "Missing FCM registration id")
    private String fcmToken;


}
