package cn.clboy.clkit.gen.service;

import cn.clboy.clkit.gen.dto.GenCrudDTO;
import cn.clboy.clkit.gen.dto.GenJavaClassDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * 生成服务
 *
 * @author clboy
 * @date 2024/04/23 15:30:16
 */
public interface GenService<T> {

    /**
     * 生crud
     *
     * @param dto      DTO
     * @param response 响应
     */
    void genCrud(GenCrudDTO dto, HttpServletResponse response);

    /**
     * gen Java类
     *
     * @param dto      DTO
     * @param response 响应
     */
    void genJavaClass(GenJavaClassDTO dto, HttpServletResponse response);

    /**
     * gen包装器Java类
     *
     * @param sourceCode 源代码
     */
    String genWrapperJavaClass(String sourceCode);

    default <S> S aa(T t, S s) {
        return null;
    }

}
