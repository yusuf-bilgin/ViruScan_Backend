package com.example.viruscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.viruscan.entity.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
    // SHA-256 değerlerini karşılaştırmak için kullanılacak.
    FileDB findBySha256Hash(String name);
}
