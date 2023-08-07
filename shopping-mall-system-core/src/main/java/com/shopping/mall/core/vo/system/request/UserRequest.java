package com.shopping.mall.core.vo.system.request;

import com.shopping.mall.core.interfaces.RequestFilter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

/**
 * <pre>
 * Description : 사용자 Request
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Schema(name = "UserRequest")
public class UserRequest {

    @Getter
    @Setter
    @EqualsAndHashCode
    @Schema(name = "UserRequestPagingFilter", description = "계정 페이징 필터")
    @ParameterObject
    public static class PagingFilter implements RequestFilter {

        @Parameter(description = "아이디")
        private String loginId;

        @Parameter(description = "회원_명")
        private String userNm;

    }
}
