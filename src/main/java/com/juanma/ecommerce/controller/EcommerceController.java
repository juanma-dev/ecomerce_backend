package com.juanma.ecommerce.controller;

import com.juanma.ecommerce.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/ecommerce")
public class EcommerceController {
    @Autowired
    UserRepository repository;
    @Autowired
    StuffRepository stuffRepository;

    @GetMapping("/user/{uid:\\d+}")
    public Optional<User> getUser(@PathVariable("uid") long uid) {
        return repository.findById(uid);
    }
    @GetMapping("/user/{uname:[^\\d]+}")
    public Optional<User> getUserByName(@PathVariable("uname") String uname) {
        return repository.findByUname(uname);
    }

    @GetMapping("/user/{uid:\\d+}/stuffs")
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
    @PostMapping("/user")
    public String putUser(@RequestBody User user) {
        Optional<User> optUser = repository.findById(user.getUid());
        repository.save(user);
        return optUser.isPresent() ? "USER UPDATED" : "USER CREATED";
    }

    @DeleteMapping("/user/{uid}")
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
    @PostMapping("/stuff")
    public String putStuff(@RequestBody Stuff stuff) {
        Optional<Stuff> optStuff = stuffRepository.findById(stuff.getSid());
        stuffRepository.save(stuff);
        return optStuff.isPresent() ? "STUFF UPDATED" : "STUFF CREATED";
    }

    @DeleteMapping("/stuff/{sid}")
    public String deleteStuff(@PathVariable("sid") long sid) {
        if (stuffRepository.existsById(sid)) {
            stuffRepository.deleteById(sid);
            return "STUFF DELETED";
        }
        return "INVALID STUFF ID";
    }


}
