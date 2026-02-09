package com.example.off;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootTestController {
    @GetMapping("/")
    public String root(){
        return "rootOK";
    }
}
