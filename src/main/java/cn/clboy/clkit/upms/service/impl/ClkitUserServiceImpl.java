package cn.clboy.clkit.upms.service.impl;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.constants.PermissionConstant;
import cn.clboy.clkit.common.service.CrudServiceImpl;
import cn.clboy.clkit.common.util.SecurityUtils;
import cn.clboy.clkit.upms.dto.UserDTO;
import cn.clboy.clkit.upms.dto.UserPasswordDTO;
import cn.clboy.clkit.upms.entity.ClkitUser;
import cn.clboy.clkit.upms.entity.Role;
import cn.clboy.clkit.upms.query.UserQuery;
import cn.clboy.clkit.upms.repository.ClkitUserRepository;
import cn.clboy.clkit.upms.service.ClkitUserService;
import cn.clboy.clkit.upms.service.RoleService;
import cn.clboy.clkit.upms.vo.UserInfoVO;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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
public class ClkitUserServiceImpl extends CrudServiceImpl<ClkitUser, Long, ClkitUserRepository>
        implements ClkitUserService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public Page<ClkitUser> getPage(Pageable page, UserQuery query) {
        return this.repository.findAll(builder -> {
        }, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClkitUser saveByDto(UserDTO dto) {
        ClkitUser user = new ClkitUser();
        this.fillUserWithDto(user, dto);
        this.save(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClkitUser updateByIdWithDto(Long id, UserDTO dto) {
        ClkitUser user = getById(id);
        boolean beforeIsAdmin = this.isAdminUser(user);
        this.fillUserWithDto(user, dto);
        boolean nowIsAdmin = this.isAdminUser(user);
        if (beforeIsAdmin && !nowIsAdmin) {
            //删除了管理员角色，需保证除此用户外还存在拥有管理员角色的用户
            List<Long> adminUidList = this.repository.findUserIdByRoleCode(PermissionConstant.ADMIN_ROLE_CODE);
            adminUidList.remove(id);
            Assert.notEmpty(adminUidList, "该用户是唯一拥有管理员角色的用户,禁止编辑其管理员角色");
        }
        this.updateById(user);
        return user;
    }

    @Override
    public String resetUserPassword(Long id) {
        ClkitUser user = getById(id);
        String password = RandomUtil.randomString(8);
        user.setPassword(passwordEncoder.encode(password));
        this.repository.save(user);
        return password;
    }

    @Override
    public void updatePassword(UserPasswordDTO dto) {
        Long userId = SecurityUtils.getLoginUserNonNull().getUserId();
        ClkitUser user = this.getById(userId);
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("原密码不正确");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.repository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        ClkitUser user = this.getById(id);
        if (this.isAdminUser(user)) {
            //需保证除此用户外还存在拥有管理员角色的用户
            List<Long> adminUidList = this.repository.findUserIdByRoleCode(PermissionConstant.ADMIN_ROLE_CODE);
            adminUidList.remove(id);
            Assert.notEmpty(adminUidList, "该用户是唯一拥有管理员角色的用户,禁止删除");
        }
        this.repository.deleteById(id);
    }

    /**
     * 使用dto填充user
     *
     * @param user 用户
     * @param dto  DTO
     */
    private void fillUserWithDto(ClkitUser user, UserDTO dto) {
        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        user.setRealName(dto.getRealName());
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setEmail(dto.getEmail());
        if (CollectionUtils.isEmpty(dto.getRoleIds())) {
            user.setRole(Collections.emptyList());
        } else {
            user.setRole(roleService.getByIds(dto.getRoleIds()));
        }
    }

    /**
     * 是管理员用户
     *
     * @param user 用户
     */
    private boolean isAdminUser(ClkitUser user) {
        if (CollectionUtils.isEmpty(user.getRole())) {
            return false;
        }
        return user.getRole().stream().map(Role::getCode).anyMatch(PermissionConstant.ADMIN_ROLE_CODE::equals);
    }
}
