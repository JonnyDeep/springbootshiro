package cn.hlq.controller;

import cn.hlq.model.User;
import cn.hlq.service.UserService;
import cn.hlq.web.EditUserCommand;
import cn.hlq.web.EditUserValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManagerUsersController {

    private static Logger logger = LoggerFactory.getLogger(ManagerUsersController.class);
    private EditUserValidator editUserValidator = new EditUserValidator();

    @Autowired
    private UserService userService;

    @RequestMapping("/manageUsers")
    @RequiresPermissions("user:manage")
    public void manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
    }

    @RequestMapping(value="/editUser",method= RequestMethod.GET)
    @RequiresPermissions("user:edit")
    public String showEditUserForm(Model model, @RequestParam Long userId, @ModelAttribute EditUserCommand command) {

        logger.info("--->get edituser");
        User user = userService.getUser( userId );
        command.setUserId(userId);
        command.setUsername(user.getUsername());
        command.setEmail(user.getEmail());
        return "editUser";
    }

    @RequestMapping(value="/editUser",method= RequestMethod.POST)
    @RequiresPermissions("user:edit")
    public String editUser(Model model, @RequestParam Long userId, @ModelAttribute EditUserCommand command, BindingResult errors) {
        logger.info("--->post useredit");
        editUserValidator.validate(command, errors);

        if( errors.hasErrors() ) {
            return "editUser";
        }

        User user = userService.getUser( userId );
        command.updateUser( user );

        userService.updateUser( user );

        return "redirect:/s/manageUsers";
    }

    @RequestMapping("/deleteUser")
    @RequiresPermissions("user:delete")
    public String deleteUser(@RequestParam Long userId) {
        Assert.isTrue( userId != 1, "Cannot delete admin user" );
        userService.deleteUser( userId );
        return "redirect:/s/manageUsers";
    }
}
