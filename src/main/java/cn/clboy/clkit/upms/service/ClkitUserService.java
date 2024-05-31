package cn.clboy.clkit.upms.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.upms.dto.UserDTO;
import cn.clboy.clkit.upms.dto.UserPasswordDTO;
import cn.clboy.clkit.upms.entity.ClkitUser;
import cn.clboy.clkit.upms.query.UserQuery;
import cn.clboy.clkit.upms.vo.UserInfoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * clKit用户服务
 *
 * @author clboy
 * @date 2024/05/17 16:32:17
 */
public interface ClkitUserService extends CrudService<ClkitUser, Long> {

    /**
     * 获取通过名称
     *
     * @param name 名称
     */
    Optional<ClkitUser> getByName(String name);

    /**
     * 获取用户信息
     */
    UserInfoVO getUserInfo();

    /**
     * 获取分页
     *
     * @param page  分页
     * @param query 查询
     */
    Page<ClkitUser> getPage(Pageable page, UserQuery query);

    /**
     * 新用户
     *
     * @param dto DTO
     */
    ClkitUser saveByDto(UserDTO dto);

    /**
     * 通过id和dto更新
     *
     * @param id  ID
     * @param dto DTO
     */
    ClkitUser updateByIdWithDto(Long id, UserDTO dto);

    /**
     * 重置用户密码
     *
     * @param id ID
     */
    String resetUserPassword(Long id);

    /**
     * 更新密码
     *
     * @param dto DTO
     */
    void updatePassword(UserPasswordDTO dto);

}
