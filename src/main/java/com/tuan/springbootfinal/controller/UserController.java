package com.tuan.springbootfinal.controller;

import com.tuan.springbootfinal.request.user.SignUpReq;
import com.tuan.springbootfinal.response.user.UserRes;
import com.tuan.springbootfinal.response.WrapRes;
import com.tuan.springbootfinal.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     @PostMapping("/authen")
        public WrapRes<UserRes> authen( @RequestBody SignUpReq signUpReq) {
            log.info("Sign up request: {}", signUpReq);
            UserRes user = userService.authenUser(signUpReq );
            return WrapRes.success(user);
        }


}
