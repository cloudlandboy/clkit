package cn.clboy.clkit.common.entity;

/**
 * 版本实体
 *
 * @author clboy
 * @date 2024/05/11 09:18:02
 */
public interface IVersionEntity {

    /**
     * 获取版本
     */
    Integer getVersion();

    /**
     * 设置版本
     *
     * @param version 版本
     */
    void setVersion(Integer version);
}
