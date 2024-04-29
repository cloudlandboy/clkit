package cn.clboy.clkit.gen.service;

import cn.clboy.clkit.gen.dto.GenCrudDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * 生成服务
 *
 * @author clboy
 * @date 2024/04/23 15:30:16
 */
public interface GenService {

    /**
     * 生crud
     *
     * @param dto      DTO
     * @param response 响应
     */
    void genCrud(GenCrudDTO dto, HttpServletResponse response);
}
