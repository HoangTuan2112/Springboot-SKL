package com.tuan.springbootfinal.controller;

import com.tuan.springbootfinal.request.user.SignUpReq;
import com.tuan.springbootfinal.response.user.UserRes;
import com.tuan.springbootfinal.response.WrapRes;
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
    @PostMapping("/sign-up")
     public WrapRes<UserRes> signUp( @Valid @RequestBody SignUpReq signUpReq) {
         log.info("Sign up request: {}", signUpReq);
         UserRes  user= UserRes.builder()
                 .username(signUpReq.getUsername())
                 .email(signUpReq.getEmail())
                 .build();
         return WrapRes.success(user);
     }

}
