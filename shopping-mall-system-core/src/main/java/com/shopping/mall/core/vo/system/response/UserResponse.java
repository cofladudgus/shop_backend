package com.shopping.mall.core.vo.system.response;

import com.shopping.mall.core.entity.system.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Description : 사용자 Response
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserResponse {

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    @Schema(name = "UserResponsePaging", description = "사용자 페이징 응답", type = "object")
    public static class Paging extends User {

        @Serial
        private static final long serialVersionUID = 4291818751186198797L;

        @Parameter(description = "NO")
        private Integer no;
    }
}
