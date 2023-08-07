package com.shopping.mall.api.controller.common;

import com.shopping.mall.api.service.common.AuthTokenService;
import com.shopping.mall.api.service.common.JwtService;
import com.shopping.mall.api.service.system.UserService;
import com.shopping.mall.core.annotatioins.NotAuthRequired;
import com.shopping.mall.core.entity.common.AuthToken;
import com.shopping.mall.core.entity.common.JwtAuthToken;
import com.shopping.mall.core.entity.system.User;
import com.shopping.mall.core.enums.ApiResultCode;
import com.shopping.mall.core.enums.JwtSessionAttribute;
import com.shopping.mall.core.vo.common.request.LoginRequest;
import com.shopping.mall.core.vo.common.response.ApiResponse;
import com.shopping.mall.core.vo.common.response.LoginResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Description : 인증 Controller
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserService userService;

    @NotAuthRequired
    @PostMapping(value = "/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {

        log.debug(">>>>>> request = {}", request);

        LoginResponse loginResponse = new LoginResponse();

        // 사용자_조회 (by LoginId)
        User user = userService.selectUserByLoginId(request.getLoginId());

        if (user == null) {
            log.warn(">>>>>> 로그인 || 사용자 NOT FOUND || request = {}", request);
            return new ApiResponse<>(ApiResultCode.NOT_FOUND_USER);
        }

        // 비밀번호 확인
//        String encPassword = null;

        /*try {
            encPassword = CryptUtil.getHash(request.getPassword(), request.getLoginId().getBytes());

            if (!user.getLoginPass().equals(encPassword)) {
                log.warn(">>>>>> 로그인 || 비밀번호 불일치 || request = {}", request);
                return new ApiResponse<>(ApiResultCode.NOT_MATCHED_USER);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ApiResponse<>(ApiResultCode.ENC_ERROR);
        }*/

        // JWT Token 생성
        JwtAuthToken token = jwtService.createAuthToken(user);

        AuthToken authToken = new AuthToken();
        authToken.setUserId(user.getId());
        authToken.setAccessToken(token.getAccessToken());
        authToken.setAccessTokenExpireDateTime(token.getAccessTokenExpireDate());
        authToken.setRefreshToken(token.getRefreshToken());
        authToken.setRefreshTokenExpireDateTime(token.getRefreshTokenExpireDate());

        // 인증토큰_등록
        authTokenService.createAuthToken(authToken);

        // 최종 결과값 엔티티 생성
        loginResponse.setUser(user);
        loginResponse.setToken(token);

        return new ApiResponse<>(ApiResultCode.SUCCESS, loginResponse);
    }

    @GetMapping(value = "/refresh")
    public ApiResponse<JwtAuthToken> refresh(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = (String) request.getAttribute(JwtSessionAttribute.ACCESS_TOKEN.name());

        // 인증토큰 갱신
        JwtAuthToken token = jwtService.refreshAuthToken(accessToken);

        if (token == null) {
            return new ApiResponse<>(ApiResultCode.DATA_NOT_FOUND);
        }

        String msg = token.getErrMessage();
        if (!msg.equals("OK")) {
            return new ApiResponse<>(ApiResultCode.TOKEN_UNAVAILABLE);
        }

        Cookie rt = new Cookie("refresh_token", token.getRefreshToken());

        rt.setMaxAge(7 * 24 * 60 * 60);
        rt.setHttpOnly(true);
        rt.setPath("/");
        response.addCookie(rt);

        return new ApiResponse<>(ApiResultCode.SUCCESS, token);
    }
}
