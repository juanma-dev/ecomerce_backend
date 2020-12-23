package com.juanma.ecommerce.controller;

import com.juanma.ecommerce.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/ecommerce")
public class EcommerceController {

    @Autowired
    UserRepository repository;
    @Autowired
    StuffRepository stuffRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    PhotosRepository photosRepository;
    @Autowired
    AuthorityRepo authorityRepo;

    @GetMapping("/user/{uid:\\d+}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Optional<User> getUser(@PathVariable("uid") long uid) {
        return repository.findById(uid);
    }
    @GetMapping("/user/{uname:[^\\d]+}")
    public Optional<User> getUserByName(@PathVariable("uname") String uname) {
        return repository.findByUname(uname);
    }

    @GetMapping("/user/{uid:\\d+}/stuffs")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Stuff> getUserStaffs(@PathVariable("uid") long uid) {
        List<Stuff> stuffs = new ArrayList<>();
        Optional<User> user = repository.findById(uid);
        user.ifPresent((u)-> stuffs.addAll( u.getStuffs()));
        return stuffs;
    }

    /*
    {
        "uid": 1,
        "uname": "juanma",
        "password": "123",
        "email": "johnimmanuelx@gmail.com",
        "photoPath": "/my/path"
    }
    * */
    /*@PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String putUser(@RequestBody User user) {
        Optional<User> optUser = repository.findById(user.getUid());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return optUser.isPresent() ? "USER UPDATED" : "USER CREATED";
    }*/
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> putUser(@RequestParam("uid") long uid,
                          @RequestParam("uname") String uname,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("authorities") String authorities){
        User user = new User();
        user.setUid(uid);
        user.setUname(uname);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setPhotoPath(uploadFileService.uploadUserPhoto(uid, file));
        Optional<User> optUser = repository.findById(uid);
        user.setAuthorities(boundAuthorities(authorities));
        repository.save(user);
        return new ResponseEntity<>(
                optUser.isPresent() ? "USER UPDATED" : "USER CREATED",
                HttpStatus.OK);
    }

    @DeleteMapping("/user/{uid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteUser(@PathVariable("uid") long uid) {
        if (repository.existsById(uid)) {
            repository.deleteById(uid);
            return "USER DELETED";
        }
        return "INVALID USER ID";
    }

    /*
    {
        "sid": 1,
        "sname": "Red Jacket",
        "category": "cloth",
        "price":10.20,
        "photoPath": "/my/path",
        "user": { "uid": 1 }
    }
    * */
    /*@PostMapping("/stuff")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String putStuff(@RequestBody Stuff stuff) {
        Optional<Stuff> optStuff = stuffRepository.findById(stuff.getSid());
        stuffRepository.save(stuff);
        return optStuff.isPresent() ? "STUFF UPDATED" : "STUFF CREATED";
    }*/
    @PostMapping("/stuff")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> putStuff(@RequestParam("sid") long sid,
                           @RequestParam("sname") String sname,
                           @RequestParam("category") String category,
                           @RequestParam("price") double price,
                           @RequestParam("files") MultipartFile[] files,
                           @RequestParam("uid") long uid) {
        Stuff stuff = new Stuff();
        stuff.setSid(sid);
        stuff.setSname(sname);
        stuff.setCategory(category);
        stuff.setPrice(price);
        stuff.setUser(repository.findById(uid).orElseThrow(InvalidParameterException::new));
        Optional<Stuff> optStuff = stuffRepository.findById(sid);
        stuffRepository.save(stuff);

        Set<String> photos = uploadFileService.uploadStuffPhotos(sid, files);
        photos.stream().forEach(path -> {
           StuffPhoto stuffPhoto = new StuffPhoto(path, stuff);
           photosRepository.save(stuffPhoto);
        });

        return new ResponseEntity<>(
                optStuff.isPresent() ? "STUFF UPDATED" : "STUFF CREATED",
                HttpStatus.OK);
    }


    @DeleteMapping("/stuff/{sid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteStuff(@PathVariable("sid") long sid) {
        if (stuffRepository.existsById(sid)) {
            stuffRepository.deleteById(sid);
            return "STUFF DELETED";
        }
        return "INVALID STUFF ID";
    }

    @GetMapping("/users/{uid:[\\d]+}/{img_name}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Resource> getUserImage(@PathVariable("uid") long uid,
                                                   @PathVariable("img_name") String imageName) {
        Path path = Paths.get(String.format(uploadFileService.getUserPathFormat(),uid, imageName));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        FileSystemResource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(path.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/stuffs/{sid:[\\d]+}/{img_name}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Resource> getStuffImages(@PathVariable("sid") long sid,
                                                   @PathVariable("img_name") String imageName) {
        Path path = Paths.get(String.format(uploadFileService.getStuffPathFormat(),sid, imageName));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        FileSystemResource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(path.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private Set<Authority> boundAuthorities(String authorities) {
        authorities = authorities
                        .replaceAll("\\s", "")
                        .toUpperCase();
        String[] auths = authorities.split(",");
        return Arrays.stream(auths).map(aname -> {
            Authority authority = authorityRepo.findByAname("ROLE_" + aname)
                    .orElseThrow(InvalidParameterException::new);
            return authority;
        }).collect(Collectors.toSet());

    }

}
