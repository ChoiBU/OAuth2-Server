package com.example.springbootoauth2server.OAuth.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CREATE TABLE oauth_refresh_tokens (
 *   token             VARCHAR(400)    NOT NULL PRIMARY KEY,
 *   client_id         VARCHAR(32)     NOT NULL,
 *   username          VARCHAR(40)     NOT NULL,
 *   access_token      VARCHAR(400),
 *   expired_at        TIMESTAMP       NOT NULL,
 *   created_at        TIMESTAMP       NOT NULL,
 * );
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // @Entity를 위해 필요함. private여도 insert는 작동하나 public/protected로 하라고 나옴
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // @Builder를 위해 필요함
@Entity
@Table(name = "oauth_refresh_tokens")
public class RefreshTokenEntity implements com.byeongukchoi.oauth2.server.entity.RefreshToken {
    @Id
    @Column(length = 54)
    private String token;
    private String clientId;
    private String username;
    private String accessToken;
    private int expiredAt;
    private int createdAt;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Boolean isExpired() {
        int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
        return currentTimestamp > expiredAt;
    }

    @Override
    public int getExpiresIn() {
        return expiredAt - createdAt;
    }

    public void expire() {
        // 현재 시간 (타임스탬프 (초))
        int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
        expiredAt = currentTimestamp - 1;
    }
}
