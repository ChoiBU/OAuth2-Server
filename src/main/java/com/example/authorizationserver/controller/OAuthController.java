package com.example.authorizationserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class OAuthController {

    /**
     * 인증 코드 요청
     * 로그인 되어있지 않으면 로그인 페이지로 리다이렉트 시킴
     * @param clientId 앱 생성 시 발급 받은 REST API 키
     * @param redirectUri 코드를 리다이렉트 해줄 URI
     * @param responseType "code"로 고정
     */
    @GetMapping("/oauth/authorize")
    public void requestAuth(@RequestParam("client_id") String clientId,
                              @RequestParam("redirect_uri") String redirectUri,
                              @RequestParam("response_type") String responseType,
                              RedirectAttributes redirectAttributes,
                              HttpServletResponse response) throws IOException {
        // TODO: 테스트로 clientId, response_type 검사하지 않음

        response.sendRedirect("/login");
    }
    @PostMapping("/oauth/token")
    public void getToken(@RequestParam("grant_type") String grantType,
                         @RequestParam("client_id") String clientId,
                         @RequestParam("redirect_uri") String redirectUri,
                         @RequestParam("code") String code) {
    }
}