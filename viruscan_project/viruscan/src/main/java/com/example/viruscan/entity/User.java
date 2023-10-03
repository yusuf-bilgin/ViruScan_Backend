package com.example.viruscan.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    private String userName;

    @Column(name = "lastName")
    private String userSurname;

    @Column(name = "email", unique = true)
    private String mailAddress;

    @Column(name = "password")
    private String password;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getUserSurname() { return userSurname; }

    public void setUserSurname(String userSurname) { this.userSurname = userSurname; }

    public String getMailAddress() { return mailAddress; }

    public void setMailAddress(String mailAddress) { this.mailAddress = mailAddress; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
