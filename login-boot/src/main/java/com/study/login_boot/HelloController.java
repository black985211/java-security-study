package com.study.login_boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring";
    }

    @GetMapping(value="/greet",produces = "text/plain")
    public String greet(@RequestParam String name){
//        return "Hello "+HtmlUtils.htmlEscape(name);
        return "Hello "+name;
    }

}
