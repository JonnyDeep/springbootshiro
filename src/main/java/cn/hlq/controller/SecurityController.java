package cn.hlq.controller;

import cn.hlq.web.LoginCommand;
import cn.hlq.web.LoginValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

    private static Logger logger = LoggerFactory.getLogger(SecurityController.class);
    private LoginValidator loginValidator = new LoginValidator();

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String showLoginForm(Model model, @ModelAttribute LoginCommand command ) {
        logger.info(command.getUsername()+" "+command.getPassword() );
        return "login";
    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    public String login(Model model, @ModelAttribute LoginCommand command, BindingResult errors) {
        loginValidator.validate(command, errors);

        logger.info("--->second");
        if( errors.hasErrors() ) {
            return showLoginForm(model, command);
        }

        UsernamePasswordToken token = new UsernamePasswordToken(command.getUsername(), command.getPassword(), command.isRemeberMe());
        try {
            Subject current = SecurityUtils.getSubject();
            current.login(token);
            Session session = current.getSession();
            logger.info("--->session:"+session.getId());
        } catch (AuthenticationException e) {
            errors.reject( "error.login.generic", "Invalid username or password.  Please try again." );
        }

        if( errors.hasErrors() ) {
            logger.info(errors.toString());
            return showLoginForm(model, command);
        } else {
            return "redirect:/s/home";
        }
    }

    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}
