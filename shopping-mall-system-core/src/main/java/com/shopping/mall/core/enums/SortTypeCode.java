package com.shopping.mall.core.enums;

import com.shopping.mall.core.interfaces.CodeEnum;
import com.shopping.mall.core.interfaces.CodeEnumTypeHandler;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.type.MappedTypes;

/**
 * <pre>
 * Description : 정렬 방법 코드
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
public enum SortTypeCode implements CodeEnum {
    DESC("DESC", "내림차순"),
    ASC("ASC", "오름차순"),
    ;

    private String code;
    private String message;

    private SortTypeCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * get Code
     *
     * @return code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * get Message
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    private static final Map<String, SortTypeCode> LOOKUP = new HashMap<>();

    static {
        for (SortTypeCode type : EnumSet.allOf(SortTypeCode.class)) {
            LOOKUP.put(type.getCode(), type);
        }
    }

    /**
     * MybatisConfigure.java typeHandlers 에 추가 필요함
     */
    @MappedTypes(SortTypeCode.class)
    public static class TypeHandler extends CodeEnumTypeHandler<SortTypeCode> {

        public TypeHandler() {
            super(SortTypeCode.class);
        }
    }

    /**
     * get ResultCode using code
     *
     * @param code code
     * @return ResultCode object
     */
    public static SortTypeCode get(int code) {
        return LOOKUP.get(code);
    }
}
