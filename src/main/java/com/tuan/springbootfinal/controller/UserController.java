package com.tuan.springbootfinal.controller;

import com.tuan.springbootfinal.request.user.SignUpReq;
import com.tuan.springbootfinal.response.user.UserRes;
import com.tuan.springbootfinal.response.WrapRes;
import com.tuan.springbootfinal.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Log4j2

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

     @PostMapping("/sign-up")
     public WrapRes<UserRes> signUp( @Valid @RequestBody SignUpReq signUpReq) {
         log.info("Sign up request: {}", signUpReq);
         UserRes user = userService.createUserRepository(signUpReq);
         return WrapRes.success(user);
     }
     //change status by otp
     @PostMapping("/authen")
     public WrapRes<UserRes> authen( @RequestBody SignUpReq signUpReq) {
        log.info("Sign up request: {}", signUpReq);
        UserRes user = userService.authenUser(signUpReq );
        return WrapRes.success(user);
    }

    //login
    @PostMapping("/login")
    public WrapRes<UserRes> login( @RequestBody SignUpReq signUpReq) {
        log.info("Sign up request: {}", signUpReq);
        String user = userService.login(signUpReq);
        return WrapRes.login(user);
    }
    //logout
    @PostMapping("/logout")
    public WrapRes<UserRes> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return WrapRes.success(null);
    }
    //get profile by token
    @GetMapping("/profile")
    public WrapRes<UserRes> getProfile(@RequestHeader("Authorization") String token) {
        log.info("Token: {}", token);
        UserRes user = userService.getProfile(token);
        return WrapRes.success(user);
    }





}
