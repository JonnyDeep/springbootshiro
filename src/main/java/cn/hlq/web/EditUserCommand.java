package cn.hlq.web;

import cn.hlq.model.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class EditUserCommand {
    private Long userId;
    private String username;
    private String email;
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateUser(User user) {
        Assert.isTrue( userId.equals( user.getId() ), "User ID of command must match the user being updated." );
        user.setUsername( getUsername() );
        user.setEmail( getEmail() );
        if( StringUtils.hasText(getPassword()) ) {
            user.setPassword( new Sha256Hash(getPassword()).toHex() );
        }
    }
}
