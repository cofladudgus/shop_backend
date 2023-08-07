package com.shopping.mall.api.service.system;

import com.shopping.mall.core.dao.system.UserDao;
import com.shopping.mall.core.entity.system.User;
import com.shopping.mall.core.vo.common.request.PageRequest;
import com.shopping.mall.core.vo.common.response.PageResponse;
import com.shopping.mall.core.vo.system.request.UserRequest;
import com.shopping.mall.core.vo.system.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * Description : 사용자 Service
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Service
@Transactional(transactionManager = "dataTxManager")
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 사용자관리_목록_조회 (페이징)
     *
     * @param pageRequest
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageResponse<UserResponse.Paging> selectUserPaging(PageRequest<UserRequest.PagingFilter> pageRequest) {
        return userDao.selectUserPaging(pageRequest);
    }

    /**
     * 사용자_조회 (by Id)
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User selectUserById(Integer userId) {
        return userDao.selectUserById(userId);
    }

    /**
     * 사용자_조회 (by LoginId)
     *
     * @param loginId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User selectUserByLoginId(String loginId) {
        return userDao.selectUserByLoginId(loginId);
    }
}
