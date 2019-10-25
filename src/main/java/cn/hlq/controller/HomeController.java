package cn.hlq.controller;

import cn.hlq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {


    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public void viewHome(Model model){
        model.addAttribute("users",userService.getAllUsers());
    }
}
