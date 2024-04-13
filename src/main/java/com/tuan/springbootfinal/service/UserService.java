package com.tuan.springbootfinal.service;

import com.tuan.springbootfinal.entity.EmailDetails;
import com.tuan.springbootfinal.entity.User;
import com.tuan.springbootfinal.enums.UserStatus;
import com.tuan.springbootfinal.repository.UserRepository;
import com.tuan.springbootfinal.request.user.SignUpReq;
import com.tuan.springbootfinal.response.user.UserRes;
import com.tuan.springbootfinal.until.MapperUntils;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    public UserRes createUserRepository(SignUpReq signUpReq){
       log.info("signUpReq: {}" , signUpReq);
        //check trung  username ,email, phone
        List<User> users= new ArrayList<>();
        users=userRepository.findAll();
        log.info("users: {}", users);
       Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsernameOrEmailOrPhone(signUpReq.getUsername(), signUpReq.getEmail(), signUpReq.getPhone()));

        if(userOptional.isPresent()){
          if(userOptional.get().getUsername().equals(signUpReq.getUsername())){
              throw new RuntimeException("Username is already taken");
          }
            else if(userOptional.get().getEmail().equals(signUpReq.getEmail())){
                throw new RuntimeException("Email is already taken");
            }
            else if(userOptional.get().getPhone().equals(signUpReq.getPhone())){
                throw new RuntimeException("Phone is already taken");
            }
        }

        String otp = String.valueOf(new Random().nextInt(999999));
        User user = User.builder()
                .username(signUpReq.getUsername())
                .email(signUpReq.getEmail())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .status(UserStatus.WAITING_CONFIRM)
                .phone(signUpReq.getPhone())
                .otp(otp)
                .build();
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setMsgBody("Your OTP for account verification is: " + otp);
        emailDetails.setSubject("Verify Your Account");
        emailService.sendSimpleMail(emailDetails);
       user= userRepository.save(user);
        return MapperUntils.mapObject(user, UserRes.class);
    }
    public UserRes authenUser(SignUpReq signUpReq){
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsernameOrPasswordOrOtp(signUpReq.getUsername(), passwordEncoder.encode(signUpReq.getPassword()), signUpReq.getOtp()));
        if(userOptional.isEmpty()){
            throw new RuntimeException("Username or password or otp is incorrect");
        }
        User user = userOptional.get();
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        return MapperUntils.mapObject(user, UserRes.class);

    }
}
