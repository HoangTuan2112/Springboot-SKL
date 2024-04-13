package com.tuan.springbootfinal.response.user;

import com.tuan.springbootfinal.enums.UserStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRes {
    private String id;
    private String username;
    private String email;
    private String phone;
    private String name;
    private String avt;
    private UserStatus status;
}
