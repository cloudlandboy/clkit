package cn.clboy.clkit.os.handler;

import cn.clboy.clkit.os.metadata.LanHostMetadata;
import cn.clboy.clkit.os.vo.PidInfoVO;
import cn.hutool.system.OsInfo;

import java.util.List;

/**
 * os处理程序
 *
 * @author clboy
 * @date 2024/04/22 13:41:31
 */
public interface OsHandler {
    OsInfo OS_INFO = new OsInfo();

    /**
     * 查找pid信息
     *
     * @param pid PID
     */
    PidInfoVO findPidInfo(Integer pid);

    /**
     * 按端口查找pid信息
     *
     * @param port 端口
     */
    PidInfoVO findPidInfoByPort(Integer port);

    /**
     * 按名称查找pid信息
     *
     * @param name 名称
     */
    List<PidInfoVO> findPidInfoByName(String name);

    /**
     * 打开url
     *
     * @param url URL
     */
    void openUrl(String url);

    /**
     * 获取ARP信息
     *
     * @param assignedIp 指派IP
     */
    List<LanHostMetadata> getArpInfo(String assignedIp);
}
