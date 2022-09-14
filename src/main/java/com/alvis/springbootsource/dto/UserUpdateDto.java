package com.alvis.springbootsource.dto;

import com.alvis.springbootsource.util.NullOrNotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserUpdateDto {
    @Email
    private String email;

    @NullOrNotBlank(message = "Blank name")
    private String name;
}
