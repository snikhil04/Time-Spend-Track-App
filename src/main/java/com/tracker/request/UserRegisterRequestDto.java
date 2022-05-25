package com.tracker.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserRegisterRequestDto {

    @Size(min = 5, max = 30, message = "name should contains min 5 characters..")
    private String name;
    @Email(regexp = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$", message = "only @gmail is accepted!")
    private String email;
    @Size(min = 8, max = 20, message = "name should contains min 5 characters..")
    private String password;

    public UserRegisterRequestDto() {
    }

    public UserRegisterRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRegisterRequestDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
