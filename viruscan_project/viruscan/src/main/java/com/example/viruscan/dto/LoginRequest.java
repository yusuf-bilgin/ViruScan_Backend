package com.example.viruscan.dto;

public class LoginRequest {
    private String mailAddress;
    private String password;

    public String getMailAddress() { return mailAddress; }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

