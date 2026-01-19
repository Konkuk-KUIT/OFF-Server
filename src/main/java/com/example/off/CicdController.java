package com.example.off;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local")
public class CicdController {

    @GetMapping("/test")
    public String localServerTest() {
        return "'version 1.0.0'";
    }
}