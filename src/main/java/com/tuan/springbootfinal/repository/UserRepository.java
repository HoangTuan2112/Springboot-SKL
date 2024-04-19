package com.tuan.springbootfinal.repository;

import com.tuan.springbootfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    public User findByUsernameOrEmailOrPhone(String username, String email, String phone);


     User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    Optional<User> findById(UUID userId);
}
