package cn.clboy.clkit.net.vo;

import lombok.Data;

import java.util.List;

/**
 * 互联网信息数据
 *
 * @author clboy
 * @date 2024/05/04 10:16:12
 */
@Data
public class InternetInfoVO {

    /**
     * 内网ip列表
     */
    private List<String> lanIpList;

    /**
     * 公网ip
     */
    private String internetIp;

}
