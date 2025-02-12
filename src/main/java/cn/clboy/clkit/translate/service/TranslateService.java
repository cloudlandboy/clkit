package cn.clboy.clkit.translate.service;

import cn.clboy.clkit.translate.dto.TranslateDTO;

/**
 * 翻译服务
 *
 * @author clboy
 * @date 2024/09/25 14:54:51
 */
public interface TranslateService {

    /**
     * 获取key
     */
    String getKey();

    /**
     * 翻译
     *
     * @param dto DTO
     */
    String translate(TranslateDTO dto);
}
