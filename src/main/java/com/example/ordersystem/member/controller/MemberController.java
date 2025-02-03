package com.example.ordersystem.member.controller;

import com.example.ordersystem.common.dto.LoginDto;
import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.dto.MemberResDto;
import com.example.ordersystem.member.dto.MemberSaveReqDto;
import com.example.ordersystem.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody MemberSaveReqDto memberSaveReqDto){
        Long memberId = memberService.save(memberSaveReqDto).getId();

        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }

    @GetMapping("list")
    public ResponseEntity<?> list(){
        List<MemberResDto> memberListResDtoList = memberService.findAll();
        return new ResponseEntity<>(memberListResDtoList, HttpStatus.OK);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto loginDto){
//        email, password 검증
        Member member = memberService.login(loginDto);

//        토큰 생성 및 검증
        String jwtToken = jwtTokenProvider.createToken();
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
