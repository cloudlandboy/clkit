package cn.clboy.clkit.os.vo;

import lombok.Data;

/**
 * PID信息数据
 *
 * @author clboy
 * @date 2024/04/22 11:37:02
 */
@Data
public class PidInfoVO {

    /**
     * PID
     */
    private Integer pid;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * 内存使用
     */
    private Long memUsage;
}
