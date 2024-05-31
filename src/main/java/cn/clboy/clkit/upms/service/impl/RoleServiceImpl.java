package cn.clboy.clkit.upms.service.impl;

import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.constants.PermissionConstant;
import cn.clboy.clkit.common.service.CrudServiceImpl;
import cn.clboy.clkit.upms.dto.RoleDTO;
import cn.clboy.clkit.upms.entity.Role;
import cn.clboy.clkit.upms.query.RoleQuery;
import cn.clboy.clkit.upms.repository.RoleRepository;
import cn.clboy.clkit.upms.service.PermissionService;
import cn.clboy.clkit.upms.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;


/**
 * role service impl
 *
 * @author clboy
 * @date 2024/05/27 10:33:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends CrudServiceImpl<Role, Long, RoleRepository> implements RoleService {

    private final PermissionService permissionService;

    @Override
    public Page<Role> getPage(Pageable page, RoleQuery query) {
        return this.repository.findAll(builder -> {
        }, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role saveByDto(RoleDTO dto) {
        Role role = new Role();
        this.fillRoleWithDto(role, dto);
        this.save(role);
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role updateByIdWithDto(Long id, RoleDTO dto) {
        Role role = getById(id);
        Assert.isTrue(!PermissionConstant.ADMIN_ROLE_CODE.equals(role.getCode()),
                "禁止编辑管理员角色");
        this.fillRoleWithDto(role, dto);
        updateById(role);
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        Role role = getById(id);
        Assert.isTrue(!PermissionConstant.ADMIN_ROLE_CODE.equals(role.getCode()),
                "禁止删除管理员角色");
        super.removeById(id);
    }

    /**
     * 用dto填充角色
     *
     * @param role role
     * @param dto  DTO
     */
    private void fillRoleWithDto(Role role, RoleDTO dto) {
        dto.setCode(dto.getCode().toUpperCase());
        if (!dto.getCode().startsWith(ClkitConstant.ROLE_CODE_PREFIX)) {
            dto.setCode(ClkitConstant.ROLE_CODE_PREFIX + dto.getCode());
        }
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        if (CollectionUtils.isEmpty(dto.getPermissionIds())) {
            role.setPermission(Collections.emptyList());
        } else {
            role.setPermission(permissionService.getByIds(dto.getPermissionIds()));
        }
    }

}
