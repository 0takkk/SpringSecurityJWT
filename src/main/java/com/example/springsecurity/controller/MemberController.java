package com.example.springsecurity.controller;

import com.example.springsecurity.common.config.security.jwt.JwtAuthenticationFilter;
import com.example.springsecurity.dto.LoginDto;
import com.example.springsecurity.dto.MemberDto;
import com.example.springsecurity.dto.ReIssueDto;
import com.example.springsecurity.dto.TokenDto;
import com.example.springsecurity.service.LoginService;
import com.example.springsecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/non")
    public ResponseEntity<String> non(){
        return ResponseEntity.ok("non");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto){
        TokenDto tokenDto = loginService.login(loginDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, headers, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> singup(@Valid @RequestBody MemberDto memberDto){
        memberService.signup(memberDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reIssue(@RequestBody ReIssueDto reIssueDto){
        log.info("controller reIssue");
        TokenDto tokenDto = loginService.reIssue(reIssueDto);
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }




}
