package com.alvis.springbootsource.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AdminCreateDto {
    @NotNull(message = "Missing email")
    @Email()
    private String email;

    @NotEmpty(message = "Missing password")
    private String password;

    @NotBlank(message = "Blank name")
    private String name;
}
