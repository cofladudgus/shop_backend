package com.shopping.mall.api.controller.common;

import com.shopping.mall.core.enums.ApiResultCode;
import com.shopping.mall.core.vo.common.response.ResponseObject;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 * Description : 에러 Controller
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@ControllerAdvice
@Controller
@Hidden
@RequestMapping("${server.error.path:/error}")
public class ErrorController extends AbstractErrorController {

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * JSON용 에러페이지 표출
     *
     * @param request
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ResponseObject<Null> error(HttpServletRequest request) {

        Map<String, Object> errorInfo = this.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE));
        log.error(">>>>> ERROR JSON: {}", errorInfo);

        ResponseObject<Null> response = new ResponseObject<>();
        try {
            ApiResultCode resultCode = ApiResultCode.get((Integer) errorInfo.get("status"));

            if (resultCode != null) {
                response.setResultCode(resultCode);
                response.setResultMessage(resultCode.getResultMessage());
            } else {
                response.setResultCode(ApiResultCode.INTERNAL_SERVER_ERROR);
                response.setResultMessage(errorInfo.toString());
                errorInfo.remove("timestamp");
            }
        } catch (Exception e) {
            response.setResultCode(ApiResultCode.INTERNAL_SERVER_ERROR);
            response.setResultMessage(e.getMessage());
            errorInfo.remove("timestamp");
        }

        return response;
    }
}
