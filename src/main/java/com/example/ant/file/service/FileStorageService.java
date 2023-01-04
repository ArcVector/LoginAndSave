package com.example.ant.file.service;

import com.example.ant.file.model.File;
import com.example.ant.login.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public File storeFile(User user, MultipartFile file);

    public File getFile(String fileId);

}
