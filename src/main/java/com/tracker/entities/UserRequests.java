package com.tracker.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String UserEmail;

    private long requestedAmount;

    private long UserCurrentBalance;

    public UserRequests() {
    }

    public UserRequests(String userEmail, long requestedAmount) {
        UserEmail = userEmail;
        this.requestedAmount = requestedAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public long getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(long requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public long getUserCurrentBalance() {
        return UserCurrentBalance;
    }

    public void setUserCurrentBalance(long userCurrentBalance) {
        UserCurrentBalance = userCurrentBalance;
    }

    @Override
    public String toString() {
        return "UserRequests{" +
                "id=" + id +
                ", UserEmail='" + UserEmail + '\'' +
                ", requestedAmount=" + requestedAmount +
                ", UserCurrentBalance=" + UserCurrentBalance +
                '}';
    }
}
