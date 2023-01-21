package com.example.springsecurity.service;

import com.example.springsecurity.common.config.security.jwt.JwtTokenProvider;
import com.example.springsecurity.domain.Member;
import com.example.springsecurity.domain.redis.RedisKey;
import com.example.springsecurity.dto.LoginDto;
import com.example.springsecurity.dto.ReIssueDto;
import com.example.springsecurity.dto.TokenDto;
import com.example.springsecurity.exception.InvalidRefreshTokenException;
import com.example.springsecurity.exception.MemberNotFoundException;
import com.example.springsecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.springsecurity.domain.redis.RedisKey.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;
    private final MemberRepository memberRepository;


    public TokenDto login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String accessToken = jwtTokenProvider.createAccessToken(authenticate);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisService.setDataWithExpiration(REFRESH_TOKEN.getKey() + authenticate.getName(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto reIssue(ReIssueDto reIssueDto){
        String findRefreshToken = redisService.getData(REFRESH_TOKEN.getKey() + reIssueDto.getName());
        if(findRefreshToken == null || !findRefreshToken.equals(reIssueDto.getRefreshToken())){
            log.info("refreshToken이 일치하지 않습니다.");
            throw new InvalidRefreshTokenException();
        }

        Member member = memberRepository.findByName(reIssueDto.getName())
                .orElseThrow(() -> new MemberNotFoundException());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken();
        redisService.setDataWithExpiration(REFRESH_TOKEN.getKey() + member.getName(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
