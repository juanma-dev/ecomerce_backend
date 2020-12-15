package com.juanma.ecommerce.controller;

import com.juanma.ecommerce.model.Stuff;
import com.juanma.ecommerce.model.StuffRepository;
import com.juanma.ecommerce.model.User;
import com.juanma.ecommerce.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/ecommerce")
public class EcommerceController {
    @Autowired
    UserRepository repository;
    @Autowired
    StuffRepository stuffRepository;

    @GetMapping("/user/{uid}")
    public List<Stuff> getUserStaffs(@PathVariable("uid") long uid) {
        List<Stuff> stuffs = new ArrayList<Stuff>();
        Optional<User> user = repository.findById(uid);
        user.ifPresent((u)-> stuffs.addAll(u.getStuffs()));
        return stuffs;
    }

    @PostMapping("/user/{uid}")
    public String putUser(@PathVariable("uid") long uid, @RequestBody Stuff stuff) {
        Optional<User> optUser = repository.findById(uid);
        User user = optUser.orElseThrow(InvalidParameterException::new);
        user.getStuffs().add(stuff);
        repository.save(user);
        return "STUFFS UPDATED";
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
    public String putUser(@RequestBody Stuff stuff) {
        Optional<Stuff> optStuff = stuffRepository.findById(stuff.getSid());
        stuffRepository.save(stuff);
        return optStuff.isPresent() ? "STUFF UPDATED" : "STUFF CREATED";
    }

}
