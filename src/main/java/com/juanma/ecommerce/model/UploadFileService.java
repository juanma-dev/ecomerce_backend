package com.juanma.ecommerce.model;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UploadFileService {
    private final String ROOT_PATH_USERS = "Uploads/Users";
    private final String ROOT_PATH_STUFFS = "Uploads/Stuffs";
    private final String USER_PIC_TAG = "user_pic";
    private final String STUFF_PIC_TAG = "stuff_pic_";
    private final String EXTENSION_REGEX = ".+(?=\\.[^\\.]+$)";

    public String uploadUserPhoto(long uid, MultipartFile file) {
        String extension = file.getOriginalFilename().replaceFirst(EXTENSION_REGEX, "");
        Path dirPath = Paths.get(ROOT_PATH_USERS, String.valueOf(uid));
        Path filePath = Paths.get(ROOT_PATH_USERS, String.valueOf(uid), USER_PIC_TAG+extension);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath.toString();
    }

    public Set<String> uploadStuffPhotos(long sid, MultipartFile[] files) {
        Path dirPath = Paths.get(ROOT_PATH_STUFFS, String.valueOf(sid));
        Set<String> photosPath = new HashSet<>();

        try {
            if (!Files.exists(dirPath)) { Files.createDirectory(dirPath); }
            for (int i=0; i<files.length; i++) {
                String extension = files[i].getName().replaceFirst(EXTENSION_REGEX, "");
                Path filePath = Paths.get(ROOT_PATH_STUFFS, String.valueOf(sid), STUFF_PIC_TAG+i+extension);
                Files.write(filePath, files[i].getBytes());
                photosPath.add(filePath.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Bad stuff pic");
        }
        return photosPath;
    }
}
