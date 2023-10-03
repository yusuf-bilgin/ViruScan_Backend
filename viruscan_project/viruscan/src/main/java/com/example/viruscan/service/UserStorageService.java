package com.example.viruscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.viruscan.entity.User;
import com.example.viruscan.repository.UserRepository;

@Service
public class UserStorageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SHA256Service sha256Service;

    public User registerUser(String userName, String userSurname, String mailAddress, String password) {

        String hashedPassword = sha256Service.calculateHash(password.getBytes());

        // Yeni kullanıcı oluşturdum
        User newUser = new User();

        newUser.setUserName(userName);
        newUser.setUserSurname(userSurname);
        newUser.setMailAddress(mailAddress);

        newUser.setPassword(hashedPassword);

        // Yeni kullanıcıyı veritabanına kaydet
        return userRepository.save(newUser);
    }

    public User loginUser(String mailAddress, String password) {

        // Kullanıcının girdiği şifreyi hashler ve veritabanındaki ilgili alanla karşılaştırır
        String hashedInputPassword = sha256Service.calculateHash(password.getBytes());

        User user = userRepository.findByMailAddressAndPassword(mailAddress,hashedInputPassword);

        // Kullanıcı bulunmazsa veya şifre eşleşmiyorsa null döndürür.
        if (user == null || !isPasswordCorrect(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    private boolean isPasswordCorrect(String password, String hashedPassword) {

        String hashedInputPassword = sha256Service.calculateHash(password.getBytes());

        return hashedInputPassword.equals(hashedPassword);
    }


}