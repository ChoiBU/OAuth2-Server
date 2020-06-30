package com.example.authorizationserver.authentication.service;

import com.example.authorizationserver.member.domain.Member;
import com.example.authorizationserver.member.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 로그인
     * @param username 사용자 아이디
     * @param password 사용자 패스워드
     * @return
     */
    public boolean login(String username, String password) throws Exception {
        Member member = memberRepository.findByUsername(username);
        if(member == null) {
            return false;
        }
        boolean isCorrectpassword = member.validatePassword(password);
        // 패스워드 틀릴 시 에러처리
        if(isCorrectpassword == false) {
            throw new Exception();
        }

        // TODO: authorization code 생성 및 insert

        return true;
    }
}
