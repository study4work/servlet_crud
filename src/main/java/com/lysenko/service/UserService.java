package com.lysenko.service;

import com.lysenko.entity.User;
import com.lysenko.repository.UserRepository;
import com.lysenko.repository.hibernateImpl.UserRepositoryImpl;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
       this.userRepository  = new UserRepositoryImpl();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user, Integer id) {
        return userRepository.update(id, user);
    }

    public User findById(Integer id) {
        return userRepository.findById(User.class, id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Integer id) {
        userRepository.delete(User.class, id);
    }
}
