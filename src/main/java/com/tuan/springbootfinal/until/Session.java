package com.tuan.springbootfinal.until;

import com.tuan.springbootfinal.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class Session {
    private String sessionId;
    private User user;
    private Date createdAt;
    private Date expiresAt;

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
        this.createdAt = new Date();
        // Thiết lập thời gian hết hạn của phiên làm việc, ví dụ 10 ngày sau khi tạo
        this.expiresAt = new Date(this.createdAt.getTime() + 10 * 24 * 60 * 60 * 1000); // 10 ngày
    }
    //isExpired()
    public boolean isExpired() {
        return new Date().after(this.expiresAt);
    }
}
