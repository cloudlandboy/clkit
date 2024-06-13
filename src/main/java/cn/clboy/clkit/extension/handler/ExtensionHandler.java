package cn.clboy.clkit.extension.handler;

import cn.clboy.clkit.common.constants.enums.ExtensionTypeEnum;
import cn.clboy.clkit.common.holder.HandlerHolder;
import cn.clboy.clkit.extension.entity.Extension;

/**
 * 扩展处理程序
 *
 * @author clboy
 * @date 2024/05/07 09:15:16
 */
public interface ExtensionHandler {
    HandlerHolder<ExtensionTypeEnum, ExtensionHandler> HOLDER = new HandlerHolder<>();

    /**
     * 获取类型
     */
    ExtensionTypeEnum getType();

    /**
     * 验证
     *
     * @param extension 扩展
     */
    void validate(Extension extension);

    /**
     * 安装
     *
     * @param extension 扩展
     */
    String install(Extension extension);
}
