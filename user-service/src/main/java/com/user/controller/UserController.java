package com.user.controller;

import com.user.entity.User;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {

        User user = this.userService.getUser(userId);
        String url = "http://contact-service/contact/user/";
        List contacts = this.restTemplate.getForObject(url + user.getUserId(), List.class);
        user.setContacts(contacts);
        return user;

    }

    @GetMapping("/allusers")
    public List getAllUser() {
        List<User> userList = userService.getAllUser();
        String url = "http://contact-service/contact/user/";
        for(User user : userList) {
            List contacts = this.restTemplate.getForObject(url + user.getUserId(), List.class);
            user.setContacts(contacts);
        }
        return userList;
    }

    @PostMapping(value = "/saveUser", consumes = "application/json", produces = "application/json")
    public boolean saveUser(@RequestBody User user) {
        User user1 = new User(user.getUserId(),user.getName(), user.getPhone());
        boolean status = userService.saveUser(user1);
        String url = "http://contact-service/contact/user/savecontact";
        this.restTemplate.put(url,user.getContacts());
        return status;
    }

}
