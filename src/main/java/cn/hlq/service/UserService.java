package cn.hlq.service;

import cn.hlq.model.User;

import java.util.List;

public interface UserService {
    User getCurrentUser();

    void createUser(String username, String email, String password);

    List<User> getAllUsers();

    User getUser(Long userId);

    void deleteUser(Long userId);

    void updateUser(User user);
}
