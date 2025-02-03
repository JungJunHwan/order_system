package com.example.ordersystem.member.service;

import com.example.ordersystem.common.dto.LoginDto;
import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.dto.MemberResDto;
import com.example.ordersystem.member.dto.MemberSaveReqDto;
import com.example.ordersystem.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member save(MemberSaveReqDto memberSaveReqDto){
        return memberRepository.save(memberSaveReqDto.toEntity(passwordEncoder.encode(memberSaveReqDto.getPassword())));
    }

    public List<MemberResDto> findAll(){
        return memberRepository.findAll().stream().map(a->a.listResDtoFromEntity()).toList();
    }

    public Member login(LoginDto loginDto){
//        email 존재 여부
        Member member = memberRepository.findByEmail(loginDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 이메일입니다."));
//        password 일치 여부
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}
