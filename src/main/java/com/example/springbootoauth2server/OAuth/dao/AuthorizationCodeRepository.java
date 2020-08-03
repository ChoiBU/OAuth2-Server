package com.example.springbootoauth2server.OAuth.dao;

import com.example.springbootoauth2server.OAuth.entity.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, String>, com.byeongukchoi.oauth2.server.repository.AuthorizationCodeRepository {
    AuthorizationCode findByCodeAndClientId(String code, String clientId);
}
