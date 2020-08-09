package com.example.springbootoauth2server.OAuth.service;

import com.byeongukchoi.oauth2.server.dto.AuthorizationRequestDto;
import com.byeongukchoi.oauth2.server.dto.TokenDto;
import com.byeongukchoi.oauth2.server.entity.AuthorizationCode;
import com.byeongukchoi.oauth2.server.entity.Client;
import com.byeongukchoi.oauth2.server.grant.AbstractGrant;
import com.byeongukchoi.oauth2.server.grant.AuthorizationCodeGrant;
import com.byeongukchoi.oauth2.server.grant.RefreshTokenGrant;
import com.example.springbootoauth2server.OAuth.repository.AccessTokenRepository;
import com.example.springbootoauth2server.OAuth.repository.AuthorizationCodeRepository;
import com.example.springbootoauth2server.OAuth.repository.ClientRepository;
import com.example.springbootoauth2server.OAuth.repository.RefreshTokenRepository;
import com.example.springbootoauth2server.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class OAuthService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthorizationCodeRepository authorizationCodeRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    /**
     * 로그인 여부를 파악하여 리다이렉트 시킴
     * @param request
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    public RedirectView getAuthorizationCode(HttpServletRequest request,
                                             RedirectAttributes redirectAttributes) throws Exception {

        String responseType = request.getParameter("response_type");
        if(responseType != null && ! responseType.equals("code")) {
            throw new Exception("Invalid Response type");
        }

        // 인증 서버와 로그인 서버가 다를 경우 쿠키에서 받아온 값으로 로그인 서버에 로그인 정보 확인 및 사용자 정보 가져옴. 현재는 같은 서버이므로 세션으로 확인이 가능하다.
        // 세션으로 로그인 여부 확인
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        RedirectView redirectView = new RedirectView();
        // 로그인이 되어있지 않은 경우 로그인으로 redirect. 현재 접속 uri를 넘김
        if(member == null) {
            String currentUrl = request.getRequestURL().toString() + "?" + request.getQueryString();
            redirectAttributes.addAttribute("continue", currentUrl);
            redirectView.setUrl("/login");
            return redirectView;
        }

        // 로그인이 되어있는 경우
        String clientId = request.getParameter("client_id");
        String redirectUri = request.getParameter("redirect_uri");

        Client client = clientRepository.getOne(clientId);
        // TODO: client 검증

        AuthorizationCode authorizationCode = authorizationCodeRepository.getNewCode(clientId, member.getUsername(), redirectUri);

        // authorize code insert
        authorizationCodeRepository.save(authorizationCode);

        redirectAttributes.addAttribute("code", authorizationCode.getCode());
        redirectView.setUrl(redirectUri);
        return redirectView;
    }

    /**
     * 토큰 발급 clientId, redirectUri, code, clientSecret
     * 토큰 갱신 clientId, refreshToken, clientSecret
     * @param authorizationRequestDto
     * @return
     * @throws Exception
     */
    public TokenDto issueToken(AuthorizationRequestDto authorizationRequestDto) throws Exception {

        // TODO: 1. client 검증
        Client client = clientRepository.getOne(authorizationRequestDto.getClientId());

        // 2. 토큰 발급
        // TODO: 상수로 변경하거나 함수로 변경해야함
        String grantType = authorizationRequestDto.getGrantType();
        AbstractGrant grant;
        if (grantType.equals("authorization_code")) {
            grant = new AuthorizationCodeGrant(authorizationCodeRepository, accessTokenRepository, refreshTokenRepository);
        } else if (grantType.equals("refresh_token")) {
            grant = new RefreshTokenGrant(accessTokenRepository, refreshTokenRepository);
        } else {
            throw new Exception("Invalid Grant Type");
        }
        TokenDto token = grant.issueToken(authorizationRequestDto);

        return token;
    }
}
