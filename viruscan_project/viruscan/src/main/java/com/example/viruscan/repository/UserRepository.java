package com.example.viruscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.viruscan.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByMailAddressAndPassword(String mailAddress, String password);
}

