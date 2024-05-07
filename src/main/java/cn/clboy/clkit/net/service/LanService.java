package cn.clboy.clkit.net.service;

import cn.clboy.clkit.net.query.ScanPortQuery;
import cn.clboy.clkit.net.vo.IpPortListVO;

import java.util.List;

/**
 * 局域网服务
 *
 * @author clboy
 * @date 2024/04/22 10:17:11
 */
public interface LanService {

    /**
     * 获取IP列表
     */
    List<String> getIpList();

    /**
     * 扫描ip
     */
    List<String> scanIp(String baseIp);

    /**
     * 扫描端口
     *
     * @param query 查询
     */
    List<IpPortListVO> scanPort(ScanPortQuery query);

}
