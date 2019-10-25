package cn.hlq.config;

import cn.hlq.cacheManager.RedisCacheManager;
import cn.hlq.realm.SampleRealm;
import cn.hlq.session.MySessionListener;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private static Logger logger = LoggerFactory.getLogger(ShiroConfig.class);
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        logger.info("---->initial shiro filter");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager( securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/success");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        Map<String,String> filterChainMap = new LinkedHashMap<>();
        // 配置可以匿名访问的地址，可以根据实际情况自己添加，放行一些静态资源等，anon 表示放行
        filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/imgs/**", "anon");
        filterChainMap.put("/js/**", "anon");
        filterChainMap.put("/swagger-*/**", "anon");
        filterChainMap.put("/swagger-ui.html/**", "anon");
        // 登录 URL 放行
        filterChainMap.put("/login", "anon");

        filterChainMap.put("/user/admin*","authc");
        filterChainMap.put("/user/user*","roles[user]");
        filterChainMap.put("/user/manageUser*/**","perms[user:manage]");
        filterChainMap.put("/logout","logout");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);

        return  shiroFilterFactoryBean;
    }
    @Bean
    public SampleRealm shiroRealm(){
        logger.info("---->initial shiro realm");
        SampleRealm realm = new SampleRealm();
        realm.setName("SampleRealm");
        realm.setCachingEnabled(true);
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(RedisTemplate redisTemplate){
        logger.info("---->initial shiro securityManager");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(redisCacheManager(redisTemplate));
        securityManager.setRealm(shiroRealm());

        securityManager.setSessionManager(sessionManager(redisTemplate));
//        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        logger.info("--->initial matcher");
        HashedCredentialsMatcher hashedCredentialsMatcher1 = new HashedCredentialsMatcher();
        hashedCredentialsMatcher1.setHashAlgorithmName("SHA-256");
        hashedCredentialsMatcher1.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher1;
    }

    @Bean("sessionListener")
    public MySessionListener sessionListener(){
        logger.info("---->initial session listener");
        MySessionListener sessionListener = new MySessionListener();
        return sessionListener;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator(){
        logger.info("----->initial session id generator");
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public SessionDAO sessionDAO(RedisTemplate redisTemplate){
        logger.info("---->initial session dao");
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
//        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        enterpriseCacheSessionDAO.setCacheManager(redisCacheManager(redisTemplate));
        return enterpriseCacheSessionDAO;
    }

    @Bean
    public SimpleCookie sessionIdCookie(){
        logger.info("--->initial session cookie");
        SimpleCookie simpleCookie = new SimpleCookie("sid");

        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public SessionManager sessionManager(RedisTemplate template) {
        logger.info("---->initial session Manager");
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        ((ArrayList<SessionListener>) listeners).add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
//        sessionManager.setCacheManager(ehCacheManager());
        sessionManager.setCacheManager(redisCacheManager(template));
        sessionManager.setSessionDAO(sessionDAO(template));
        return sessionManager;
    }

//    @Bean
//    public EhCacheManager ehCacheManager(){
//        logger.info("---->initial session ehCacheManager");
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ecache.xml");
//
//        return  ehCacheManager;
//    }


    /**
     *
     * redisCahae 代替ehcache
     * @param template
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate template){

        return new RedisCacheManager(template,120);
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


}
