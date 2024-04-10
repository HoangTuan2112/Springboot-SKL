package com.tuan.springbootfinal.response.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRes {
    private String username;
    private String email;
}
