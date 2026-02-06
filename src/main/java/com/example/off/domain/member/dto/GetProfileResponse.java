package com.example.off.domain.member.dto;

import com.example.off.domain.member.Member;
import com.example.off.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProfileResponse {
    private String name;
    private String nickname;
    private Boolean isWorking;
    private String project;  //내가 속한 플젝명
    private Role role;

    //Todo: project 명 찾기
    public static GetProfileResponse of(Member member){
        return new GetProfileResponse(
                member.getName(),
                member.getNickname(),
                member.getIsWorking(),
                "project명",
                member.getRole()
        );
    }
}
