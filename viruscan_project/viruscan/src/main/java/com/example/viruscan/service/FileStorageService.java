package com.example.viruscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.viruscan.dto.ResponseMessage;
import com.example.viruscan.entity.FileDB;
import com.example.viruscan.repository.FileDBRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Autowired
    private FileDBRepository fileDBRepository;
    @Autowired
    private SHA256Service sha256Service;

    private final String uploadDirectory = "C:/Users/yusuf/Desktop/Depo";

    public String getUploadDirectory() {
        return uploadDirectory;
    }

    private final String scriptDirectory = "C:\\Users\\yusuf\\Desktop\\script.ps1";

    public String getScriptDirectory() {
        return scriptDirectory;
    }

    public BufferedReader reader;

    public String sha256Hash;

    public String originalFilename;
    public String cleanedFilename;

    public void scriptOperations(MultipartFile file) throws IOException {
        // Dosyanın orijinal adını temizle
        originalFilename = file.getOriginalFilename();
        cleanedFilename = cleanFileName(originalFilename);

        // Dosyanın hedef klasöre taşı ve yeni adıyla kaydet
        Path sourceFilePath = Paths.get(getUploadDirectory(), originalFilename);
        Path targetFilePath = Paths.get(getUploadDirectory(), cleanedFilename);
        Files.move(sourceFilePath, targetFilePath);

        // Script.ps1 dosyası içine komut yaz
        File scriptFile = new File(getScriptDirectory());
        FileWriter writer = new FileWriter(scriptFile);
        writer.write("cd \"C:\\ProgramData\\Microsoft\\Windows Defender\\Platform\\4.18.23080.2006-0\"\n");
        writer.write(".\\MpCmdRun.exe -Scan -ScanType 3 -File \"C:\\Users\\yusuf\\Desktop\\Depo\\" + cleanedFilename + "\"\n");
        writer.close();

        // Powershell açma komutu (Bypass ile yetki gereksinimlerini atla)
        String command = "powershell.exe -ExecutionPolicy Bypass -File " + scriptFile.getAbsolutePath();

        // command değişkeninde verilen PowerShell komutunu başlat
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // PowerShell çıktısını okum
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public String cleanFileName(String originalFilename) {
        // Dasya adındaki Türkçe karakterleri çıkar
        String cleanedFilename = Normalizer.normalize(originalFilename, Normalizer.Form.NFD);

        // İngilizce karakterleri koru ve özel karakterleri kaldır
        cleanedFilename = cleanedFilename.replaceAll("[^\\p{ASCII}]", "");
        return cleanedFilename;
    }

    public void storeDB(MultipartFile file, String scanResult) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        sha256Hash = sha256Service.calculateHash(file.getBytes());

        // Save to database
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), scanResult, sha256Hash);
        fileDBRepository.save(fileDB);
    }

    public void storeToFolder(MultipartFile file) throws IOException {
        // Depo klasörüne kaydetme (Database'ye değil)
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(getUploadDirectory(), fileName);
        Files.copy(file.getInputStream(), filePath);
    }

    public void deleteFromFolder(String fileName) throws IOException {
        Path filePath = Paths.get(getUploadDirectory(), fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            System.out.println("Dosya başarıyla silindi: " + fileName);
        } else {
            System.out.println("Dosya bulunamadı veya silinemedi: " + fileName);
        }
    }

    public void deleteFile(String fileId) {
        // Veritabanından dosyayı sil
        fileDBRepository.deleteById(fileId);

        // Depolama alanından dosyayı sil
        File file = new File(uploadDirectory + fileId);
        if (file.exists()) {
            file.delete();
        }
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}
