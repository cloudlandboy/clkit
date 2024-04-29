package cn.clboy.clkit.os.query;


import lombok.Data;

/**
 * 扫描端口查询
 *
 * @author clboy
 * @date 2024/04/22 11:11:56
 */
@Data
public class ScanPortQuery {

    /**
     * 基于ip
     */
    private String baseIp;

    /**
     * 最小端口
     */
    private Integer minPort;

    /**
     * 最大端口
     */
    private Integer maxPort;
}
