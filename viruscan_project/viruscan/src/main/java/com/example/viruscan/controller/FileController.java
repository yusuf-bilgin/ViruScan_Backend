package com.example.viruscan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.viruscan.dto.ResponseFile;
import com.example.viruscan.dto.ResponseMessage;
import com.example.viruscan.entity.FileDB;
import com.example.viruscan.service.FileStorageService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:3000")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    String line;
    String scanResultDb;

    @PostMapping("/uploadScan")
    @CrossOrigin(origins = "http://localhost:3000/uploadfiles")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {

            // Dosyayı depolama alanına kaydet
            storageService.storeToFolder(file);

            // Powershell tarama işlemini yap
            storageService.scriptOperations(file);

            // Powershell çıktısını dosyaya yazma (Gerek olmayabilir!)
            BufferedWriter outputFile = new BufferedWriter(new FileWriter("output.txt"));

            scanResultDb = "";
            while ((line = storageService.reader.readLine()) != null) {
                scanResultDb = scanResultDb.concat(line);
                System.out.println(line); // Ekrana yazdır
                outputFile.write(line); // Dosyaya yaz
                outputFile.newLine();
            }

            outputFile.close(); // Dosyayı kapat

            // Database'e kaydetme
            storageService.storeDB(file, scanResultDb);
            storageService.deleteFromFolder(file.getOriginalFilename());

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IOException exception) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "! (Probably already exists in the directory)";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(filDB -> {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(filDB.getId()).toUriString();

            String scanResult = filDB.getScanResult();
            String status;
            if (scanResult.contains("found no threats")) {
                status = "CLEAR";
            } else if (scanResult.contains("Threat detected")) {
                status = "Virus Found!";
            } else {
                status = "CmdTool: Failed";
            }

            return new ResponseFile(filDB.getName(), fileDownloadUri, filDB.getType(), filDB.getData().length / 1024, status);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<ResponseFile> getFileDetails(@PathVariable String id) {

        FileDB fileDB = storageService.getFile(id);

        if (fileDB != null) {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(fileDB.getId()).toUriString();

            ResponseFile responseFile = new ResponseFile(fileDB.getName(), fileDownloadUri, fileDB.getType(), fileDB.getData().length / 1024, fileDB.getScanResult());

            return ResponseEntity.ok(responseFile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String id) {
        String message = "";
        try {
            // Dosyayı veritabanından ve depolama alanından sil
            storageService.deleteFile(id);
            message = "File deleted successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete the file";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


}
