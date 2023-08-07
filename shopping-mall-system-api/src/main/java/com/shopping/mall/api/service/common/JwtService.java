package com.shopping.mall.api.service.common;

import com.shopping.mall.core.dao.common.AuthTokenDao;
import com.shopping.mall.core.entity.common.AuthToken;
import com.shopping.mall.core.entity.common.JwtAuthToken;
import com.shopping.mall.core.entity.system.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * Description : JWT Service
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.access.token.secret.key}")
    private String ACCESS_SECRET_KEY;

    @Value("${jwt.access.token.expiration.time}")
    private Integer ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh.token.expiration.time}")
    private Integer REFRESH_TOKEN_EXPIRATION_TIME;

    public static final String JWT_KEY_LOINGID = "loginId";
    public static final String JWT_KEY_MEMBER_ID = "userId";
    public static final String JWT_KEY_MEMBER_NAME = "userName";

    @Autowired
    private AuthTokenDao authTokenDao;

    /**
     * JWT Token 생성
     *
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public JwtAuthToken createAuthToken(User user) {
        LocalDateTime accessTokenExpireDate = LocalDateTime.now().plusMinutes(ACCESS_TOKEN_EXPIRATION_TIME);
        LocalDateTime refreshTokenExpireDate = LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_TIME);

        // 새로운 토큰 생성
        JwtAuthToken token = new JwtAuthToken();
        token.setAccessToken(buildJwtToken(user, accessTokenExpireDate));
        token.setRefreshToken(UUID.randomUUID().toString());
        token.setAccessTokenExpireDate(accessTokenExpireDate);
        token.setRefreshTokenExpireDate(refreshTokenExpireDate);

        return token;
    }

    /**
     * JWT Token 갱신
     *
     * @param accessToken
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public JwtAuthToken refreshAuthToken(String accessToken) {

        // 토큰정보 조회
        AuthToken lastToken = authTokenDao.selectAuthTokenByAccessToken(accessToken);

        if (lastToken == null) {
            log.warn(">>>>>> 최종 토큰값 NOT FOUND");
            return new JwtAuthToken(HttpStatus.NOT_FOUND.name());
        }

        // RefreshToken 만료기간 확인
        if (lastToken.getRefreshTokenExpireDateTime().isBefore(LocalDateTime.now())) {
            log.warn(">>>>>> Refresh Token 만료");
            return new JwtAuthToken(HttpStatus.REQUEST_TIMEOUT.name());
        }

        // 파라미터로 수신한 토큰정보에서 회원정보 추출
        User user = getUser(accessToken);

        // 새로운 토큰 생성
        LocalDateTime accessTokenExpireDate = LocalDateTime.now().plusMinutes(ACCESS_TOKEN_EXPIRATION_TIME);
        LocalDateTime refreshTokenExpireDate = LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_TIME);

        JwtAuthToken newJwtAuthToken = new JwtAuthToken(HttpStatus.OK.name());
        newJwtAuthToken.setAccessToken(buildJwtToken(user, accessTokenExpireDate));
        newJwtAuthToken.setRefreshToken(UUID.randomUUID().toString());
        newJwtAuthToken.setAccessTokenExpireDate(accessTokenExpireDate);
        newJwtAuthToken.setRefreshTokenExpireDate(refreshTokenExpireDate);

        // API Token 등록
        AuthToken authToken = new AuthToken();
        authToken.setUserId(user.getId());
        authToken.setAccessToken(newJwtAuthToken.getAccessToken());
        authToken.setAccessTokenExpireDateTime(accessTokenExpireDate);
        authToken.setRefreshToken(newJwtAuthToken.getRefreshToken());
        authToken.setRefreshTokenExpireDateTime(refreshTokenExpireDate);

        authTokenDao.createAuthToken(authToken);

        return newJwtAuthToken;
    }

    /**
     * JWT Access 토큰 Builder
     *
     * @param user
     * @param accessTokenExpireDate
     * @return
     */
    private String buildJwtToken(User user, LocalDateTime accessTokenExpireDate) {
        byte[] keyBytes = (ACCESS_SECRET_KEY).getBytes(StandardCharsets.UTF_8);

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setExpiration(Date.from(accessTokenExpireDate.atZone(ZoneId.systemDefault()).toInstant()))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .claim(JWT_KEY_LOINGID, user.getLoginId())
            .claim(JWT_KEY_MEMBER_ID, user.getId())
            .claim(JWT_KEY_MEMBER_NAME, user.getUserNm())
            .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰 회원 정보를 반환
     *
     * @param accessToken
     * @return
     */
    public User getUser(String accessToken) {
        byte[] keyBytes = (ACCESS_SECRET_KEY).getBytes(StandardCharsets.UTF_8);

        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(keyBytes)
            .build()
            .parseClaimsJws(accessToken);

        User user = new User();
        user.setLoginId((String) claims.getBody().get(JWT_KEY_LOINGID));
        user.setId((Integer) claims.getBody().get(JWT_KEY_MEMBER_ID));
        user.setUserNm((String) claims.getBody().get(JWT_KEY_MEMBER_NAME));

        return user;
    }

    /**
     * 토큰 유효성 체크
     *
     * @param accessToken
     * @return
     */
    public boolean isValidToken(String accessToken) {
        byte[] keyBytes = (ACCESS_SECRET_KEY).getBytes(StandardCharsets.UTF_8);

        try {
            Jwts.parserBuilder()
                .setSigningKey(keyBytes)
                .build()
                .parseClaimsJws(accessToken);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
