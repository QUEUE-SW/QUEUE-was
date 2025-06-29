package com.queuewas.common.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenProvider {

	private final SecretKey key;
	private final long accessExpireTime;

	public AccessTokenProvider(@Value("${jwt.token.access.secret-key}") String secretKey,
		@Value("${jwt.token.access.expire-time}") long accessExpireTime) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
		this.accessExpireTime = accessExpireTime;
	}

	public String generateToken(Long studentId, String identifier) {
		Date date = new Date();
		return Jwts.builder()
			.claims(createClaims(studentId, identifier))
			.signWith(key)
			.expiration(createExpiredDate(date, accessExpireTime))
			.compact();
	}

	public Map<String, Object> createClaims(Long studentId, String identifier) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", studentId);
		claims.put("identifier", identifier);
		return claims;
	}

	public Date createExpiredDate(Date date, Long expirationTime) {
		return new Date(date.getTime() + expirationTime);
	}
}
