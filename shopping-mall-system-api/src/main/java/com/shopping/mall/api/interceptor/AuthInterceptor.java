package com.shopping.mall.api.interceptor;

import com.shopping.mall.api.service.common.AuthTokenService;
import com.shopping.mall.api.service.common.JwtService;
import com.shopping.mall.core.annotatioins.NotAuthRequired;
import com.shopping.mall.core.entity.common.AuthToken;
import com.shopping.mall.core.entity.system.User;
import com.shopping.mall.core.enums.ApiResultCode;
import com.shopping.mall.core.enums.JwtSessionAttribute;
import com.shopping.mall.core.vo.common.response.ApiResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * Description : 인증 Interceptor
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        log.debug(">>>>>> request uri = {}", request.getRequestURI());

        // BAD URI 요청의 경우 인증체크 하지 않도록 처리
        if (uri.equals("/error")) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;

            // 호출대상 핸들러가 로그인이 필요한 경우(AccessToken 필요)
            if (!method.hasMethodAnnotation(NotAuthRequired.class)) {
\
                String accessToken = request.getHeader("Authorization");

                // 인증토큰이 없으면 에러처리
                if (!StringUtils.hasLength(accessToken)) {
                    log.warn(">>>>>> {}", ApiResultCode.NOT_FOUND_TOKEN.getResultMessage());
                    response.getWriter().write(new ApiResponse<>(ApiResultCode.NOT_FOUND_TOKEN).toString());
                    return false;
                }

                // 앞에 Bearer 붙어서 오면 제거
                accessToken = accessToken.replace("Bearer ", "");

                //log.debug(">>>>> Request AccessToken = [{}]", accessToken);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                if (!StringUtils.hasLength(accessToken)) {                  // 토큰이 없는 경우 에러
                    response.getWriter().write(new ApiResponse<>(ApiResultCode.NOT_FOUND_TOKEN).toString());
                    return false;
                } else if (!jwtService.isValidToken(accessToken)) {     // 토큰 유효성 체크
                    response.getWriter().write(new ApiResponse<>(ApiResultCode.TOKEN_UNAVAILABLE).toString());
                    return false;
                }

                // 토큰 회원 정보를 반환
                User user = jwtService.getUser(accessToken);

                // 필요한 정보를 세팅
                request.setAttribute(JwtSessionAttribute.ACCESS_TOKEN.name(), accessToken);
                request.setAttribute(JwtSessionAttribute.USER_ID.name(), user.getId());
                request.setAttribute(JwtSessionAttribute.USER_LOGIN_ID.name(), user.getLoginId());

                // 토큰 위/변조 및 ExpireTime 체크
                int resultCode = checkTokenAndLifeExpire(user.getId(), accessToken);

                if (resultCode < 0) {
                    if (resultCode == -1) {
                        response.getWriter().write(new ApiResponse<>(ApiResultCode.ACCESS_TOKEN_EXPIRED).toString());
                        return false;
                    } else if (resultCode == -2) {
                        response.getWriter().write(new ApiResponse<>(ApiResultCode.TOKEN_EXPIRED).toString());
                        return false;
                    } else if (resultCode == -3) {
                        response.getWriter().write(new ApiResponse<>(ApiResultCode.TOKEN_UNAVAILABLE).toString());
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // TODO
    }

    /**
     * 토큰 위/변조 및 ExpireTime 체크
     *
     * @param userId
     * @param accessToken
     * @return
     */
    private int checkTokenAndLifeExpire(Integer userId, String accessToken) {

        LocalDateTime now = LocalDateTime.now();

        // 인증토큰_조회 (by userId)
        AuthToken authToken = authTokenService.selectAuthTokenByUserId(userId);

        // AccessToken 위/변조 체크
        if (!accessToken.equals(authToken.getAccessToken())) {
            return -3;
        }

        // AccessToken Expire 체크
        if (now.isAfter(authToken.getAccessTokenExpireDateTime())) {
            return -1;
        } else if (now.isAfter(authToken.getRefreshTokenExpireDateTime())) {
            return -2;
        }

        return 0;
    }
}
