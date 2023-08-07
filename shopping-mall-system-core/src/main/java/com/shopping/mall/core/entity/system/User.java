package com.shopping.mall.core.entity.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.GsonBuilder;
import com.shopping.mall.core.adapter.LocalDateSerializer;
import com.shopping.mall.core.adapter.LocalDateTimeSerializer;
import com.shopping.mall.core.adapter.LocalTimeSerializer;
import com.shopping.mall.core.interfaces.BaseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * Description : 사용자 Entity
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User", description = "사용자 Entity")
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -8010232973065921621L;

    @Parameter(description = "ID")
    private Integer id;

    @Parameter(description = "로그인 ID")
    private String loginId;

    @JsonIgnore
    @Parameter(description = "로그인 패스워드")
    private String loginPass;

    @Parameter(description = "사용자 명")
    private String userNm;

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        return gsonBuilder.setPrettyPrinting().create().toJson(this);
    }
}
