package com.foro.forordokotoro.Controlleurs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping("/salutation")
    public String saluer(){
        return "Bonjour keita ";
    }
}
