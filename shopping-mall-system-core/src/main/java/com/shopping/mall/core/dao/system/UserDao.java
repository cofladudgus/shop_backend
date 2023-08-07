package com.shopping.mall.core.dao.system;

import com.shopping.mall.core.entity.system.User;
import com.shopping.mall.core.vo.common.request.PageRequest;
import com.shopping.mall.core.vo.common.response.PageResponse;
import com.shopping.mall.core.vo.system.request.UserRequest;
import com.shopping.mall.core.vo.system.response.UserResponse;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * Description : 사용자 Dao
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Repository
public class UserDao {

    private final String PRE_NS = "com.shopping.mall.mapper.system.user.";

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;

    /**
     * 사용자관리_목록_조회 (페이징)
     *
     * @param pageRequest
     * @return
     */
    public PageResponse<UserResponse.Paging> selectUserPaging(PageRequest<UserRequest.PagingFilter> pageRequest) {
        List<UserResponse.Paging> users = sqlSession.selectList(PRE_NS.concat("selectUserPaging"), pageRequest);
        if (users != null && !users.isEmpty()) {
            return new PageResponse<>(pageRequest.getCurPage(), pageRequest.getPageRowCount(), users, users.get(0).getTotalRowCount());
        } else {
            return new PageResponse<>(pageRequest.getCurPage(), pageRequest.getPageRowCount());
        }
    }

    /**
     * 회원_등록
     *
     * @param user
     * @return
     */
    public Integer createUser(User user) {
        return sqlSession.insert(PRE_NS.concat("createUser"), user);
    }

    /**
     * 회원_조회 (by Id)
     *
     * @param userId
     * @return
     */
    public User selectUserById(Integer userId) {
        return sqlSession.selectOne(PRE_NS.concat("selectUserById"), userId);
    }

    /**
     * 회원조회 (by LoginId)
     *
     * @param loginId
     * @return
     */
    public User selectUserByLoginId(String loginId) {
        return sqlSession.selectOne(PRE_NS.concat("selectUserByLoginId"), loginId);
    }

    /**
     * 회원_수정
     *
     * @param user
     * @return
     */
    public Integer updateUser(User user) {
        return sqlSession.update(PRE_NS.concat("updateUser"), user);
    }
}
