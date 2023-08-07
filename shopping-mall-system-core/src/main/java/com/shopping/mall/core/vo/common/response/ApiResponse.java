package com.shopping.mall.core.vo.common.response;

import com.google.gson.GsonBuilder;
import com.shopping.mall.core.adapter.LocalDateSerializer;
import com.shopping.mall.core.adapter.LocalDateTimeSerializer;
import com.shopping.mall.core.adapter.LocalTimeSerializer;
import com.shopping.mall.core.enums.ApiResultCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description : API 기본 Response
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Getter
@Setter
@EqualsAndHashCode
public class ApiResponse<T> {

    private int resultCode;
    private String resultMessage;

    private T body;

    public ApiResponse() {
    }

    public ApiResponse(ApiResultCode apiResultCode) {
        this.resultCode = apiResultCode.getResultCode();
        this.resultMessage = apiResultCode.getResultMessage();
        this.body = null;
    }

    public ApiResponse(T body) {
        this.resultCode = ApiResultCode.getResultCode(ApiResultCode.SUCCESS.getResultCode());
        this.resultMessage = ApiResultCode.getResultMessage(ApiResultCode.SUCCESS.getResultCode());
        this.body = body;
    }

    public void setApiResultCode(ApiResultCode apiResultCode) {
        this.resultCode = apiResultCode.getResultCode();
        this.resultMessage = apiResultCode.getResultMessage();
    }

    public void setBody(T body) {
        this.body = body;
    }

    public ApiResponse(ApiResultCode apiResultCode, T body) {
        this.resultCode = apiResultCode.getResultCode();
        this.resultMessage = apiResultCode.getResultMessage();
        this.body = body;
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        return gsonBuilder.setPrettyPrinting().create().toJson(this);
    }
}
