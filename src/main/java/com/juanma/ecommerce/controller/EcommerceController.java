package com.juanma.ecommerce.controller;

import com.juanma.ecommerce.model.Staff;
import com.juanma.ecommerce.model.User;
import com.juanma.ecommerce.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/ecommerce")
public class EcommerceController {
    @Autowired
    UserRepository repository;

    @GetMapping("/{uid}")
    public List<Staff> getUserStaffs(@PathVariable("uid") long uid) {
        List<Staff>  staffs = new ArrayList<Staff>();
        Optional<User> user = repository.findById(uid);
        user.ifPresent((u)->staffs.addAll(u.getStaffs()));
        return staffs;
    }

}
