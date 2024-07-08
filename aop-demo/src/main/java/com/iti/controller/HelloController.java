package com.iti.controller;


import com.iti.annotation.My;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {
    @GetMapping("hello")
    public String sayHello(){
        System.out.println("进入了sayhello");
        return "Hello liu Nice to meet you!";
    }
    @GetMapping("hi")
    @My("Jack")
    public String sayHi(String name){
        System.out.println("hi中收到name=:" +name);;
        return "Hi" +name +",Gald to see you!";
    }
}
