package com.shopping.mall.core.vo.common.response;

import com.google.gson.GsonBuilder;
import com.shopping.mall.core.adapter.LocalDateSerializer;
import com.shopping.mall.core.adapter.LocalDateTimeSerializer;
import com.shopping.mall.core.adapter.LocalTimeSerializer;
import com.shopping.mall.core.entity.common.JwtAuthToken;
import com.shopping.mall.core.entity.system.User;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description : 로그인 Response
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Setter
@Getter
@EqualsAndHashCode
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 5394075572060316247L;

    private User user;

    private JwtAuthToken token;

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        return gsonBuilder.setPrettyPrinting().create().toJson(this);
    }
}
