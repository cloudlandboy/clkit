package cn.clboy.clkit.upms.service.impl;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import cn.clboy.clkit.common.util.SecurityUtils;
import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.upms.entity.ClkitUser;
import cn.clboy.clkit.upms.repository.ClkitUserRepository;
import cn.clboy.clkit.upms.service.ClkitUserService;
import cn.clboy.clkit.upms.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * clKit用户服务实施
 *
 * @author clboy
 * @date 2024/05/17 16:33:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClkitUserServiceImpl extends AppDataHandlerCrudServiceImpl<ClkitUser, ClkitUserRepository> implements ClkitUserService {

    @Override
    public Optional<ClkitUser> getByName(String name) {
        return this.repository.findOne(builder -> {
            builder.equal(ClkitUser::getName, name);
        });
    }

    @Override
    public UserInfoVO getUserInfo() {
        ClkitAuthUser loginUser = SecurityUtils.getLoginUserNonNull();
        ClkitUser user = this.getById(loginUser.getUserId());
        return UserInfoVO.with(user, loginUser.getAuthorities());
    }
}
