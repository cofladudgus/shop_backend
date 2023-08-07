package com.shopping.mall.core.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Description : API 결과코드 Entity
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
public enum ApiResultCode {

    SUCCESS(200, "SUCCESS"),
    DATA_NOT_FOUND(404, "데이터가 존재하지 않습니다."),
    NO_REQUIRED_VALUE(490, "필수값이 없습니다."),
    DUPLICATE_VALUE(491, "이미 존재하는 값이 있습니다."),
    FAIL(500, "FAILURE"),
    BAD_REQUEST(600, "입력값이 유효하지 않습니다."),
    DELETE_NOT_AVALIABLE(601, "데이터 삭제가 불가능합니다."),
    NOT_FOUND_TOKEN(900, "인증토큰 값이 존재하지 않습니다."),
    TOKEN_UNAVAILABLE(901, "인증토큰 값이 유효하지 않습니다."),
    ACCESS_TOKEN_EXPIRED(902, "인증토큰 값이 만료되었습니다.(Access 토큰 만료)"),
    TOKEN_EXPIRED(903, "인증토큰 값이 만료되었습니다.(Access & Refresh 토큰 만료)"),
    NOT_FOUND_USER(904, "사용자 정보가 존재하지 않습니다."),
    ENC_ERROR(920, "암복호화 에러"),
    NOT_MATCHED_USER(921, "로그인ID/비밀번호 불일치"),
    FAIL_SEND_MAIL(932, "이메일 발송 실패"),
    FAIL_SEND_AUTH_NUMBER(933, "이메일 인증번호 불일치"),
    FAIL_SEND_SMS(934, "SMS 인증번호 전송 실패"),
    FAIL_SEND_AUTH_SMS(935, "SMS 인증번호 불일치"),
    FAIL_CREATE(936, "등록 실패"),
    FAIL_UPDATE(937, "수정 실패"),
    FAIL_DELETE(938, "삭제 실패"),
    FAIL_UPLOAD_FILE(939, "업로드 파일 처리 중 에러가 발생하였습니다."),
    FAIL_UPLOAD_FILE_EXTSN(940, "허용된 확장자가 아닙니다."),
    FAIL_UPLOAD_FILE_MAXSIZE(941, "허용된 용량을 초과하였습니다."),
    FAIL_UPLOAD_FILE_NOTALLOW(942, "허용된 파일이 아닙니다."),
    DB_ERROR(998, "DB 처리 중 에러가 발생하였습니다."),
    INTERNAL_SERVER_ERROR(999, "INTERNAL_SERVER_ERROR"),
    ;

    private final int resultCode;
    private final String resultMessage;

    ApiResultCode(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }

    private static final Map<Integer, ApiResultCode> LOOKUP = new HashMap<Integer, ApiResultCode>();

    static {
        for (ApiResultCode elem : EnumSet.allOf(ApiResultCode.class)) {
            LOOKUP.put(elem.getResultCode(), elem);
        }
    }

    public static ApiResultCode get(int code) {
        return LOOKUP.get(code);
    }

    public static int getResultCode(int code) {
        return get(code).resultCode;
    }

    public static String getResultMessage(int code) {
        return get(code).resultMessage;
    }
}
