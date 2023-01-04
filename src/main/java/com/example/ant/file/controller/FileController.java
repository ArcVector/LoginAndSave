package com.example.ant.file.controller;

import com.example.ant.file.model.File;
import com.example.ant.file.payload.FileResponse;
import com.example.ant.file.service.FileStorageService;
import com.example.ant.login.model.User;
import com.example.ant.login.repository.UserRepository;
import com.example.ant.message.MessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/uploadFile")
    public FileResponse uploadFile(@RequestParam("username") String username, @RequestParam("file") MultipartFile file) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessageEnum.USERNAME_NOT_FOUND.getMessage() + username));
        File fileModel = fileStorageService.storeFile(user, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                .path(fileModel.getId()).toUriString();
        FileResponse uploadResponse = new FileResponse();
        uploadResponse.setName(fileModel.getName());
        uploadResponse.setType(file.getContentType());
        uploadResponse.setUserName(username);
        uploadResponse.setSize(file.getSize());
        uploadResponse.setDownloadUri(fileDownloadUri);
        return uploadResponse;
    }


    @PostMapping("/uploadMultipleFiles")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("username") String username, @RequestParam("files") MultipartFile[] files) {

        return Arrays.asList(files).stream().map(file -> uploadFile(username, file)).collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {

        File fileModel = fileStorageService.getFile(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileModel.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getName() + "\"")
                .body(new ByteArrayResource(fileModel.getData()));
    }
}
