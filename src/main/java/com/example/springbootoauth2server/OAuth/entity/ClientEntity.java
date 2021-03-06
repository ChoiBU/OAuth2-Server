package com.example.springbootoauth2server.OAuth.entity;

import com.byeongukchoi.oauth2.server.dto.AuthorizationRequestDto;
import com.byeongukchoi.oauth2.server.entity.Client;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CREATE TABLE oauth_clients (
 *   client_id         VARCHAR(86)      NOT NULL PRIMARY KEY,
 *   client_secret     VARCHAR(32),
 *   name              VARCHAR(64)      NOT NULL,
 *   redirect_uri      VARCHAR(500),
 *   grant_types       VARCHAR(20),
 *   username          VARCHAR(40)      NOT NULL
 * );
 */

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // @Entity를 위해 필요함. private여도 insert는 작동하나 public/protected로 하라고 나옴
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // @Builder를 위해 필요함
@Table(name = "oauth_clients")
public class ClientEntity implements Client {
    @Id
    @Column(length = 86)
    private String clientId;
    private String clientSecret;
    private String name;
    private String redirectUri;     // 카카오의 경우 redirectUri 값을 확인함
    private String grantTypes;
    private String username;

    @Override
    public boolean verifyClient(AuthorizationRequestDto authorizationRequestDto) {
        String redirectUri = authorizationRequestDto.getRedirectUri();
        if(redirectUri != null) {
            if( ! redirectUri.equals(this.redirectUri)) {
                return false;
            }
        }
        // clientSecret 같은 경우 값이 있는 경우는 검사를 해야하고, 값이 없는 경우는 clientSecret을 사용하지 않는 경우임
        if(this.clientSecret != null && clientSecret != null && this.clientSecret.equals(clientSecret)) {
            return false;
        }
        return true;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
