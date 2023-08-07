package com.shopping.mall.api.controller.system;

import com.shopping.mall.api.annotation.JwtUser;
import com.shopping.mall.api.service.system.UserService;
import com.shopping.mall.core.entity.system.User;
import com.shopping.mall.core.enums.ApiResultCode;
import com.shopping.mall.core.vo.common.request.PageRequest;
import com.shopping.mall.core.vo.common.response.ApiResponse;
import com.shopping.mall.core.vo.common.response.PageResponse;
import com.shopping.mall.core.vo.system.request.UserRequest;
import com.shopping.mall.core.vo.system.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Description : 사용자 Controller
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "사용자 관련 API")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/paging")
    @Operation(summary = "사용자관리 목록 조회 (페이징)")
    public ApiResponse<PageResponse<UserResponse.Paging>> selectUserPaging(
        @Parameter(hidden = true) @JwtUser User jwtUser,
        @Parameter(name = "UserRequestPagingFilter", description = "사용자관리 목록 조회 (페이징) 필터", in = ParameterIn.PATH) UserRequest.PagingFilter filter,
        @Parameter(name = "PageRequest", description = "사용자관리 목록 조회 페이징") PageRequest<UserRequest.PagingFilter> pageRequest
    ) {
        pageRequest.setFilter(filter);
        // 사용자관리_목록_조회 (페이징)
        PageResponse<UserResponse.Paging> response = userService.selectUserPaging(pageRequest);

        return new ApiResponse<>(ApiResultCode.SUCCESS, response);
    }

    @GetMapping(value = "/{loginId}")
    public ApiResponse<User> selectUserByLoginId(@PathVariable String loginId) {

        // 사용자_조회 (by LoginId)
        User user = userService.selectUserByLoginId(loginId);

        if (user == null) {
            return new ApiResponse<>(ApiResultCode.NOT_FOUND_USER);
        }

        return new ApiResponse<>(user);
    }
}
