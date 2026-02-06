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
    private String projectName;  //내가 속한 플젝명
    private Role role;

    public static GetProfileResponse of(Member member, String projectName){
        return new GetProfileResponse(
                member.getName(),
                member.getNickname(),
                member.getIsWorking(),
                projectName,
                member.getRole()
        );
    }
}
