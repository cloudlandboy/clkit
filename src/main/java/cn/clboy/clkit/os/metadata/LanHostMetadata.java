package cn.clboy.clkit.os.metadata;

import lombok.Data;

/**
 * arp元数据
 *
 * @author clboy
 * @date 2024/05/29 14:39:15
 */
@Data
public class LanHostMetadata {

    /**
     * IP
     */
    private String ip;

    /**
     * mac地址
     */
    private String macAddress;

    /**
     * 网络接口
     */
    private String networkInterface;
}
