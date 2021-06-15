package com.example.shop.service;

import com.example.shop.entity.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUser();
    public User getUser(Integer id);
    public User saveUser(User user);
    public User updateUser(User user);
    public User deleteUser(User user);
}
