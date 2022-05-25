package com.tracker.response;

public class MostSpendUsersResponse {

    private String id;
    private String userName;
    private String userEmail;
    private int spendmoney;

    public MostSpendUsersResponse(String id, String userName, String userEmail, int spendmoney) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.spendmoney = spendmoney;
    }

    public MostSpendUsersResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getSpendmoney() {
        return spendmoney;
    }

    public void setSpendmoney(int spendmoney) {
        this.spendmoney = spendmoney;
    }

    @Override
    public String toString() {
        return "MostSpendUsersResponse{" +
                "userid=" + id +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", spendmoney=" + spendmoney +
                '}';
    }
}
