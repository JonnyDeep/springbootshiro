package cn.hlq.service;

import cn.hlq.model.User;
import cn.hlq.persistence.UsersMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component(value = "userServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public User getCurrentUser() {
        final Long currentUserId = (long) SecurityUtils.getSubject().getPrincipal();
        if(currentUserId!=null){
            return getUser(currentUserId);
        }else{
            return null;
        }

    }

    @Override
    public void createUser(String username, String email, String password) {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUser(Long userId) {
        return usersMapper.getUser(userId);
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public void updateUser(User user) {

    }
}
