package cn.hlq.persistence;

import cn.hlq.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper {
    User getUser(Long userId);

    User findUser(String username);

    void createUser(User user);

    List<User> getAllUsers();

    void deleteUser(Long userId);

    void updateUser(User user);

    Integer insertUser(User user);
}
