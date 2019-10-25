package cn.hlq.controller;

import cn.hlq.service.UserService;
import cn.hlq.web.SignupCommand;
import cn.hlq.web.SignupValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignupController {

    private SignupValidator signupValidator = new SignupValidator();

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/signup",method= RequestMethod.GET)
    public String showSignupForm(Model model, @ModelAttribute SignupCommand command) {
        return "signup";
    }

    @RequestMapping(value="/signup",method= RequestMethod.POST)
    public String showSignupForm(Model model, @ModelAttribute SignupCommand command, BindingResult errors) {
        signupValidator.validate(command, errors);

        if( errors.hasErrors() ) {
            return showSignupForm(model, command);
        }

        // Create the user
        userService.createUser( command.getUsername(), command.getEmail(), command.getPassword() );

        // Login the newly created user
        SecurityUtils.getSubject().login(new UsernamePasswordToken(command.getUsername(), command.getPassword()));

        return "redirect:/s/home";
    }
}
