package com.tracker.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserLogin {


    @Email(regexp = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$", message = "Only Gmail accounts are accepted")
    private String email;

    @Size(min = 8, max = 20, message = "Password Should Be Min 5 Characters!")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
