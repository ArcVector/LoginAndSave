package com.example.ant.file.service;

import com.example.ant.file.exception.FileNotFoundException;
import com.example.ant.file.exception.FileStorageException;
import com.example.ant.file.model.File;
import com.example.ant.file.repository.FileRepository;
import com.example.ant.login.model.User;
import com.example.ant.message.MessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@Service

public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    private FileRepository fileRepository;
    @Override
    public File storeFile(User user, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException(MessageEnum.FILE_CONTAINS_INVALID_CHARACTERS.getMessage());
            }
            File fileModel = new File();
            fileModel.setName(fileName);
            fileModel.setType(file.getContentType());
            fileModel.setData(file.getBytes());
            fileModel.setUser(user);
            return fileRepository.save(fileModel);

        } catch (IOException e) {
            throw new FileStorageException(MessageEnum.COULD_NOT_STORE_FILE.getMessage());
        }
    }

    @Override
    public File getFile(String fileId) {
        return fileRepository.findById(fileId).orElseThrow(()-> new FileNotFoundException(MessageEnum.FILE_NOT_FOUND.getMessage()));
    }


}
