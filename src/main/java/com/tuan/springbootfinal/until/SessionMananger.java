package com.tuan.springbootfinal.until;

import com.tuan.springbootfinal.entity.User;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Component
public class SessionMananger {
    // Lưu trữ các phiên làm việc theo sessionId
    private final Map<String, Session> sessions = new HashMap<>();

    // Tạo phiên làm việc mới cho người dùng và trả về sessionId
    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, user);
        sessions.clear();
        sessions.put(sessionId, session);
        return sessionId;
    }

    // Lấy thông tin phiên làm việc dựa trên sessionId
    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    // Xóa phiên làm việc khi nó hết hạn hoặc được hủy bỏ
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
