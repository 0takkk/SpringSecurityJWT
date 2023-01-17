package com.example.springsecurity.common.jwt.member;

import com.example.springsecurity.domain.Member;
import com.example.springsecurity.exception.MemberNotFoundException;
import com.example.springsecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자가 존재하지 않습니다."));

        return MemberDetails.builder()
                .id(member.getId())
                .name(member.getName())
                .password(member.getPassword())
                .build();
    }
}
