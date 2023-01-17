package com.example.springsecurity.service;

import com.example.springsecurity.domain.Member;
import com.example.springsecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findMemberByName(String name){
        return memberRepository.findByName(name);
    }
}
