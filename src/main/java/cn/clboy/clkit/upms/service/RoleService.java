package cn.clboy.clkit.upms.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.upms.dto.RoleDTO;
import cn.clboy.clkit.upms.entity.Role;
import cn.clboy.clkit.upms.query.RoleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * 角色服务
 *
 * @author clboy
 * @date 2024/05/27 10:32:00
 */
public interface RoleService extends CrudService<Role, Long> {

    /**
     * 获取分页
     *
     * @param page  分页
     * @param query 查询
     */
    Page<Role> getPage(Pageable page, RoleQuery query);

    /**
     * 新增
     *
     * @param dto dto
     */
    Role saveByDto(RoleDTO dto);

    /**
     * 更新
     *
     * @param id  id
     * @param dto dto
     */
    Role updateByIdWithDto(Long id, RoleDTO dto);


}
