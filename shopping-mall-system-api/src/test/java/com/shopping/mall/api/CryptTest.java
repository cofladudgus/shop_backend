package com.shopping.mall.api;

import com.shopping.mall.core.utils.CryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <pre>
 * Description : 암복호화 테스트
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@ActiveProfiles("local")
@SpringBootTest
public class CryptTest {

    @Test
    void encLoginPass() {
        String loginId = "lsc9946";
        String password = "12345678";

        String encPass = null;

        try {
            encPass = CryptUtil.getHash(password, loginId.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.debug(">>>>>> enc Pass = [{}]", encPass);
    }
}
