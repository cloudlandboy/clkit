package cn.clboy.clkit.os.vo;

import lombok.Data;

import java.util.List;

/**
 * IP端口列表数据
 *
 * @author clboy
 * @date 2024/04/22 11:10:07
 */
@Data
public class IpPortListVO {

    /**
     * IP
     */
    private String ip;

    /**
     * 端口列表
     */
    private List<Integer> ports;

}
