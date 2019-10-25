package cn.hlq.controller;


import org.apache.juli.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.omg.CORBA.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.xml.ws.RequestWrapper;

@RestController
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/index")
    String home(){
        System.out.println("hello workd");
        return "index";
    }

    @RequestMapping("/login")
    public Object login(String name, String password){
        logger.info("---->login");
        Subject subject = SecurityUtils.getSubject();
        return "nihao";
    }
}
