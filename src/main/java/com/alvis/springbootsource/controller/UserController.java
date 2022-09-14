package com.alvis.springbootsource.controller;

import com.alvis.springbootsource.component.AuthFacade;
import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.model.UserModel;
import com.alvis.springbootsource.services.base.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    private final AuthFacade authFacade;

    @PostMapping("/admin")
    public UserModel createAdmin(@RequestParam("token") String token,@Valid @RequestBody AdminCreateDto dto) {
        return new UserModel(userService.createAdmin(token ,dto));
    }

    @GetMapping("/users/my")
    @ApiResponses(@ApiResponse(code = 404, message = "USER_NOT_FOUND"))
    public UserModel getMyUser() {
        var uid = authFacade.getIdentity();
        return new UserModel(userService.getUser(uid));
    }
}
