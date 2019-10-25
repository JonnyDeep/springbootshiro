package cn.hlq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(scanBasePackages = "cn.hlq")
@ComponentScan
@MapperScan("cn.hlq.persistence")
public class Main {

    @RequestMapping("/")
    String home(){
        return "hello world";
    }
    public static void main(String[] args){
        SpringApplication.run(Main.class,args);
    }
}
