package com.example.shop.controller;

import com.example.shop.entity.User;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public Iterable<User> getCustomers(){
        return userService.getAllUser();
    }

    @PostMapping("/add-user")
    public String add(@RequestParam String username, @RequestParam String password, @RequestParam String role){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.saveUser(user);
        return "Added new User";
    }

    @GetMapping("/find-user/{id}")
    public User getUserById(@PathVariable Integer id){
        return userService.getUser(id);
    }

    @PutMapping("/update-user/{id}")
    public String update(@RequestBody User newUser, @PathVariable Integer id) {

        User updatedEmployee = userService.updateUser(newUser);
        return "User has successfully updated";
    }

    @DeleteMapping("/delete-user/{id}")
    public String delete(@RequestBody User user, @PathVariable Integer id) {

        userService.deleteUser(user);

        return "User has successfully deleted";
    }
}
