package com.juanma.ecommerce.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "application.path")
public class UploadFileService {
    private String URL;
    private String rootFolder;
    private String pathUsers;
    private String pathStuffs;
    private String userPathFormat;
    private String stuffPathFormat;
    private String userPicTag;
    private String stuffPicTag;
    private String extensionRegex;

    public String uploadUserPhoto(long uid, MultipartFile file) {
        String extension = file.getOriginalFilename().replaceFirst(extensionRegex, "");
        Path dirPath = Paths.get(pathUsers, String.valueOf(uid));
        Path filePath = Paths.get(pathUsers, String.valueOf(uid), userPicTag+extension);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return translateToURL(filePath.toString());
    }

    public Set<String> uploadStuffPhotos(long sid, MultipartFile[] files) {
        Path dirPath = Paths.get(pathStuffs, String.valueOf(sid));
        Set<String> photosPath = new HashSet<>();

        try {
            if (!Files.exists(dirPath)) { Files.createDirectory(dirPath); }
            for (int i=0; i<files.length; i++) {
                String extension = files[i].getOriginalFilename().replaceFirst(extensionRegex, "");
                Path filePath = Paths.get(pathStuffs, String.valueOf(sid), stuffPicTag+i+extension);
                Files.write(filePath, files[i].getBytes());
                photosPath.add(translateToURL(filePath.toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return photosPath;
    }

    private String translateToURL(String filePath) {
        return filePath
                .replaceFirst(rootFolder, URL)
                .toLowerCase();
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public String getPathUsers() {
        return pathUsers;
    }

    public void setPathUsers(String pathUsers) {
        this.pathUsers = pathUsers;
    }

    public String getPathStuffs() {
        return pathStuffs;
    }

    public void setPathStuffs(String pathStuffs) {
        this.pathStuffs = pathStuffs;
    }

    public String getUserPathFormat() {
        return userPathFormat;
    }

    public void setUserPathFormat(String userPathFormat) {
        this.userPathFormat = userPathFormat;
    }

    public String getStuffPathFormat() {
        return stuffPathFormat;
    }

    public void setStuffPathFormat(String stuffPathFormat) {
        this.stuffPathFormat = stuffPathFormat;
    }

    public String getUserPicTag() {
        return userPicTag;
    }

    public void setUserPicTag(String userPicTag) {
        this.userPicTag = userPicTag;
    }

    public String getStuffPicTag() {
        return stuffPicTag;
    }

    public void setStuffPicTag(String stuffPicTag) {
        this.stuffPicTag = stuffPicTag;
    }

    public String getExtensionRegex() {
        return extensionRegex;
    }

    public void setExtensionRegex(String extensionRegex) {
        this.extensionRegex = extensionRegex;
    }
}
