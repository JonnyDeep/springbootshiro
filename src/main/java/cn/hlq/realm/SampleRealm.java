package cn.hlq.realm;


import cn.hlq.model.Role;
import cn.hlq.model.User;
import cn.hlq.persistence.UsersMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(SampleRealm.class);
    @Autowired
    private UsersMapper usersMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("----->授权");
        Long userId = (long) principals.fromRealm(getName()).iterator().next();
        logger.info("userId:{}",userId);
        User user = usersMapper.getUser(userId);
        if(user!=null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (Role role : user.getRoles()) {
                info.addRole(role.getName());
                info.setStringPermissions(role.getPermissions());
            }
            return info;
        }else{
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("---->验证");
        UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
        User user = usersMapper.findUser(authcToken.getUsername());
        if(user!=null){
            return new SimpleAuthenticationInfo(user.getId(),user.getPassword(),getName());
        }else{
            return null;
        }

    }


}

