package com.example.springsecurity.service;

import com.example.springsecurity.domain.Member;
import com.example.springsecurity.domain.Role;
import com.example.springsecurity.dto.MemberDto;
import com.example.springsecurity.exception.DuplicationMemberNameException;
import com.example.springsecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberDto memberDto){
        if(memberRepository.existsByName(memberDto.getName())){
            throw new DuplicationMemberNameException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .name(memberDto.getName())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .position(memberDto.getPosition())
                .role(Role.ROLE_MEMBER)
                .build();

        memberRepository.save(member);
    }

}
