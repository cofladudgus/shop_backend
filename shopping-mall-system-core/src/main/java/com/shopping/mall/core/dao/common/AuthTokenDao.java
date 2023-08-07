package com.shopping.mall.core.dao.common;

import com.shopping.mall.core.entity.common.AuthToken;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * Description : JWT 인증토큰 Dao
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Repository
public class AuthTokenDao {

    private final String PRE_NS = "com.shopping.mall.mapper.common.authToken.";

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;

    /**
     * 인증토큰 조회 (by userId)
     *
     * @param userId
     * @return
     */
    public AuthToken selectAuthTokenByUserId(Integer userId) {
        return sqlSession.selectOne(PRE_NS.concat("selectAuthTokenByUserId"), userId);
    }

    /**
     * 인증토큰 조회 (by accessToken)
     *
     * @param accessToken
     * @return
     */
    public AuthToken selectAuthTokenByAccessToken(String accessToken) {
        return sqlSession.selectOne(PRE_NS.concat("selectAuthTokenByAccessToken"), accessToken);
    }

    /**
     * 인증토큰 등록
     *
     * @param authToken
     * @return
     */
    public Integer createAuthToken(AuthToken authToken) {
        return sqlSession.insert(PRE_NS.concat("createAuthToken"), authToken);
    }

    /**
     * 인증토큰 폐기
     *
     * @param personId
     * @param accessToken
     * @return
     */
    public Integer updateTokenByLogout(Integer personId, String accessToken) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("personId", personId);
        filter.put("accessToken", accessToken);

        return sqlSession.insert(PRE_NS.concat("updateTokenByLogout"), filter);
    }
}
