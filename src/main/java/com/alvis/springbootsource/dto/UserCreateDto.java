package com.alvis.springbootsource.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserCreateDto {
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Blank name")
    private String name;
}
