package com.alvis.springbootsource.controller;

import com.alvis.springbootsource.component.AuthFacade;
import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.dto.FcmTokenDto;
import com.alvis.springbootsource.dto.UserUpdateDto;
import com.alvis.springbootsource.entity.User;
import com.alvis.springbootsource.model.UserModel;
import com.alvis.springbootsource.services.base.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = "User APIs (all)")
public class UserController {


    private final UserService userService;

    private final AuthFacade authFacade;

    @PostMapping("/auth/admin")
    public UserModel registerAdmin(@Valid @RequestBody AdminCreateDto dto) {
        return new UserModel(userService.registerAdmin(dto));
    }


    @PostMapping("/auth/login")
    public User loginOrRegister(@RequestHeader(value = HttpHeaders.AUTHORIZATION , required = false) @Nullable String headers,
                                @Valid @RequestBody() @Nullable FcmTokenDto fcmToken) {
        return userService.loginOrRegister(headers , fcmToken);
    }

    @PatchMapping("/users/my")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(code = 404, message = "USER_NOT_FOUND"),
            @ApiResponse(code = 409, message = "CANNOT_CHANGE_ADMIN_EMAIL")
    })
    public void updateMyUser(@Valid @RequestBody UserUpdateDto dto) {
        var myUid = authFacade.getIdentity();
        System.out.println(myUid);
        userService.updateUser(myUid, dto);
    }


    @GetMapping("/users/my")
    @ApiResponses(@ApiResponse(code = 404, message = "USER_NOT_FOUND"))
    public UserModel getMyUser() {
        var myUid = authFacade.getIdentity();
        return new UserModel(userService.getUser(myUid));
    }
}
