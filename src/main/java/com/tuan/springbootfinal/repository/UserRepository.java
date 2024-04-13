package com.tuan.springbootfinal.repository;

import com.tuan.springbootfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    public User findByUsernameOrEmailOrPhone(String username, String email, String phone);

    User findByUsernameAndPasswordAndOtp(String username, String password, String otp);

    User findByUsernameOrPasswordOrOtp(String username, String encode, String otp);
}
