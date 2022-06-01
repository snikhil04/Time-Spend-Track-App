package com.tracker.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDateTime LoginTime;

    private String UserId;

    private String UserEmail;

    private String Activity;

    public UserActivity() {
    }

    public UserActivity(LocalDateTime loginTime, String userId, String userEmail, String activity) {
        LoginTime = loginTime;
        UserId = userId;
        UserEmail = userEmail;
        Activity = activity;
    }

    public LocalDateTime getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        LoginTime = loginTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "id=" + id +
                ", LoginTime=" + LoginTime +
                ", UserId='" + UserId + '\'' +
                ", UserEmail='" + UserEmail + '\'' +
                ", Activity='" + Activity + '\'' +
                '}';
    }
}
