package com.example.off.domain.member.controller;

import com.example.off.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public String getMyProfile(){
        return "string";
    }

    public String updateMyProfile(){
        return "string";
    }

    public String getJoinedProjects() {
        return "string";
    }
}
