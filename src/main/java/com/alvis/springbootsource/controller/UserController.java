package com.alvis.springbootsource.controller;

import com.alvis.springbootsource.component.AuthFacade;
import com.alvis.springbootsource.dto.AdminCreateDto;
import com.alvis.springbootsource.dto.UserCreateDto;
import com.alvis.springbootsource.dto.UserUpdateDto;
import com.alvis.springbootsource.model.UserModel;
import com.alvis.springbootsource.services.base.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/admin")
    public UserModel createAdmin(@Valid @RequestBody AdminCreateDto dto) {
        return new UserModel(userService.createAdmin(dto));
    }


    @PostMapping("/users")
    @ApiResponses({
            @ApiResponse(code = 409, message = """
			USER_ALREADY_EXIST
			CANNOT_REGISTER_USER_WITHOUT_PHONE"""),
            @ApiResponse(code = 500, message = "FIREBASE_ERROR")
    })
    public UserModel registerUser(
            @RequestParam("idToken") String idToken) {
        return new UserModel(userService.createUser(idToken));
    }

    @PatchMapping("/users/my")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(code = 404, message = "USER_NOT_FOUND"),
            @ApiResponse(code = 409, message = "CANNOT_CHANGE_ADMIN_EMAIL")
    })
    public void updateMyUser(@Valid @RequestBody UserUpdateDto dto) {
        String myUid = authFacade.getIdentity();
        userService.updateUser(myUid, dto);
    }


    @GetMapping("/users/my")
    @ApiResponses(@ApiResponse(code = 404, message = "USER_NOT_FOUND"))
    public UserModel getMyUser() {
        var myUid = authFacade.getIdentity();
        return new UserModel(userService.getUser(myUid));
    }
}
