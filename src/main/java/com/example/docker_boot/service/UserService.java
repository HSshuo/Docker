package com.example.docker_boot.service;

import com.example.docker_boot.entity.User;

/**
 * @author SHshuo
 * @data 2022/11/25--16:49
 */
public interface UserService {
    void addUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    User findUserById(Integer id);
}
