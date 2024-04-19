package com.tuan.springbootfinal.service;

import com.tuan.springbootfinal.entity.EmailDetails;
import com.tuan.springbootfinal.entity.User;
import com.tuan.springbootfinal.enums.UserStatus;
import com.tuan.springbootfinal.exception.NotFoundException;
import com.tuan.springbootfinal.exception.ServiceException;
import com.tuan.springbootfinal.exception.UnauthorizedException;
import com.tuan.springbootfinal.repository.UserRepository;
import com.tuan.springbootfinal.request.user.SignUpReq;
import com.tuan.springbootfinal.response.user.UserRes;
import com.tuan.springbootfinal.until.JWT;
import com.tuan.springbootfinal.until.MapperUntils;
import com.tuan.springbootfinal.until.Session;
import com.tuan.springbootfinal.until.SessionMananger;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

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

       Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsernameOrEmailOrPhone(signUpReq.getUsername(), signUpReq.getEmail(), signUpReq.getPhone()));

        if(userOptional.isPresent()){
          if(userOptional.get().getUsername().equals(signUpReq.getUsername())){
              throw new ServiceException("Username is already exits");
          }
            else if(userOptional.get().getEmail().equals(signUpReq.getEmail())){
                throw new ServiceException("Email is already exits");
            }
            else if(userOptional.get().getPhone().equals(signUpReq.getPhone())){
                throw new ServiceException("Phone is already exits");
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
        User user= userRepository.findByUsername(signUpReq.getUsername());

        log.info("user: {}", user);
        if(user==null){
            throw new ServiceException("Username incorrect");
        }
        if(user.getOtp().equals(signUpReq.getOtp()))
        {

            user.setStatus(UserStatus.ACTIVE);
            user = userRepository.save(user);
        }
        else {
            throw new ServiceException("OTP incorrect");
        }



        return MapperUntils.mapObject(user, UserRes.class);

    }
    public List<UserRes> getAllUser(){
        List<User> users = userRepository.findAll();
        List<UserRes> userRes = new ArrayList<>();
        for(User user: users){
            userRes.add(MapperUntils.mapObject(user, UserRes.class));
        }
        return userRes;
    }
//   //login service
//    public UserRes login(SignUpReq signUpReq){
//        User user = userRepository.findByUsername(signUpReq.getUsername());
//        if(user==null){
//            throw new ServiceException("Username incorrect");
//        }
//        if(!passwordEncoder.matches(signUpReq.getPassword(), user.getPassword())){
//            throw new ServiceException("Password incorrect");
//        }
//        return MapperUntils.mapObject(user, UserRes.class);
//    }
//    //get Profile by id
//    public UserRes getProfile(String id){
//        UUID userId = UUID.fromString(id);
//
//        Optional<User> user = userRepository.findById(userId);
//        if(user.isEmpty()){
//            throw new NotFoundException("User not found");
//        }
//        return MapperUntils.mapObject(user.get(), UserRes.class);
//    }
    @Autowired
    public JWT JWTUntils;
    @Autowired
    private SessionMananger sessionMananger;

    public String login(SignUpReq signUpReq){
        User user = userRepository.findByUsername(signUpReq.getUsername());
        if(user==null){
            throw new ServiceException("Username incorrect");
        }
        if(!passwordEncoder.matches(signUpReq.getPassword(), user.getPassword())){
            throw new ServiceException("Password incorrect");
        }

       String sessionID = sessionMananger.createSession(user);
        Session session = sessionMananger.getSession(sessionID);
        return JWTUntils.generateToken(session.getSessionId());
    }
    public UserRes getProfile(String token){
        // Xác minh JWT và lấy thông tin phiên làm việc
        String sessionId = JWTUntils.extractSessionId(token);
        Session session = sessionMananger.getSession(sessionId);

        // Kiểm tra tính hợp lệ của phiên làm việc
        if (session == null || session.isExpired()) {
            throw new UnauthorizedException("Session is invalid or expired");
        }

        // Trả về thông tin người dùng
        return MapperUntils.mapObject(session.getUser(), UserRes.class);
    }
}
