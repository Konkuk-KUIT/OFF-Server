package com.example.off.domain.member.service;

import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.dto.LoginRequest;
import com.example.off.domain.member.dto.LoginResponse;
import com.example.off.domain.member.dto.SignupRequest;
import com.example.off.domain.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    public final MemberRepository memberRepository;

    @Transactional
    public void signup(@Valid SignupRequest signupRequest) {
        //이메일 중복 검증
        if(memberRepository.existByEmail(signupRequest.getEmail())) {
            throw new DuplicateEmailException();
        }

        //Todo: pw 암호화
//        String encodedPassword = passwordEncoder.encode(request.password());

        //회원 생성
        Member member = Member.of(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                signupRequest.getNickname(),
                signupRequest.getSelfIntroduction(),
                signupRequest.getBirth(),
                signupRequest.getProjectCount(),
                signupRequest.getProfileImage()
        );

        //포트폴리오 생성 및 연관 관계 설정
        if(signupRequest.getPortfolioList()!=null){
            for (SignupRequest.PortfolioRequest pr : signupRequest.getPortfolioList()) {
                Portfolio portfolio = Portfolio.create(pr.getDescription(), pr.getLink(), member);
                member.addPortfolio(portfolio);
            }
        }

        //DB 저장
        memberRepository.save(member);
    }

    @Transactional
    public LoginResponse login(@Valid LoginRequest loginRequest){
        Member member = memberRepository.findByEmail(loginRequest.email()).orElseThrow(AuthenticationFailedException::new);

        //비밀번호 검증
        //Todo: passwordEncoder 도입
        if (!loginRequest.password().equals(member.getPassword())) {
            throw new AuthenticationFailedException();
        }

        //Todo: 토큰 발급
        return new LoginResponse("accessToken", "tokenType");
    }


    private static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException() {
            super(ResponseCode.DUPLICATE_EMAIL.getMessage());
        }
    }

    private static class AuthenticationFailedException extends RuntimeException{
        public AuthenticationFailedException() {super(ResponseCode.INVALID_LOGIN_CREDENTIALS.getMessage());}
    }
}
