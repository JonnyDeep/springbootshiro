package cn.hlq.service;

import cn.hlq.Main;
import cn.hlq.model.Role;
import cn.hlq.model.User;
import cn.hlq.persistence.RolesMapper;
import cn.hlq.persistence.UsersMapper;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired UserService userService;
    @Test
    public void test1(){
        User user = new User();
        user.setId(3l);
        user.setUsername("userTest");  //admin
        user.setEmail("sample@shiro.apache.org");
        user.setPassword(new Sha256Hash("userTest").toHex());  //admin
        int i = usersMapper.insertUser(user);

        System.out.println(i);
    }

    @Test
    public void testInsertRole(){
        Role role_one = new Role();
        role_one.setId(1l);
        role_one.setName("user");
        role_one.setDescription("The default role given to all users.");
        Role role_two = new Role();
        role_two.setId(2l);
        role_two.setName("admin");
        role_two.setDescription("The administrator role only given to site admins')");
        rolesMapper.insertRoles(role_one);
        rolesMapper.insertRoles(role_two);
    }

    @Test
    public void testQueryUser(){
       logger.info("----test query User");
        User user =  userService.getCurrentUser();
       logger.info(user.toString());
    }

}
