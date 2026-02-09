package com.example.off.domain.member.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.Portfolio;
import com.example.off.domain.member.dto.*;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    public final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(@Valid SignupRequest signupRequest) {
        //이메일 중복 검증
        if(memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new OffException(ResponseCode.DUPLICATE_EMAIL);
        }

        //pw 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        //회원 생성
        Member member = Member.of(
                signupRequest.getName(),
                signupRequest.getEmail(),
                encodedPassword,
                signupRequest.getNickname(),
                signupRequest.getRole(),
                signupRequest.getSelfIntroduction(),
                signupRequest.getBirth(),
                signupRequest.getProjectCount(),
                signupRequest.getProfileImage()
        );

        //포트폴리오 생성 및 연관 관계 설정
        if(signupRequest.getPortfolioList()!=null){
            for (PortfolioRequest pr : signupRequest.getPortfolioList()) {
                Portfolio portfolio = Portfolio.of(pr.description(), pr.link());
                member.addPortfolio(portfolio);
            }
        }

        //DB 저장
        Member savedMember = memberRepository.save(member);
        return SignupResponse.of(savedMember);
    }

    @Transactional
    public LoginResponse login(@Valid LoginRequest loginRequest){
        Member member = memberRepository.findByEmail(loginRequest.email()).orElseThrow(()->new OffException(ResponseCode.INVALID_LOGIN_CREDENTIALS));

        //비밀번호 검증
        if(!passwordEncoder.matches(
                loginRequest.password(),
                member.getPassword()
        )) {
            throw new OffException(ResponseCode.INVALID_LOGIN_CREDENTIALS);
        }

        //jwt 토큰 발급
        String accessToken = jwtTokenProvider.createToken(
                member.getId().toString(),
                member.getRole().toString()
        );
        return LoginResponse.of(accessToken, "Bearer");
    }
}
