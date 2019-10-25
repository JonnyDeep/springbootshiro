package cn.hlq.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public class UserController {

    @PostMapping("/admin")
    public void amdin(){
        System.out.println("hello");
    }
}
