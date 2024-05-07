package cn.clboy.clkit.net.service.impl;

import cn.clboy.clkit.net.query.ScanPortQuery;
import cn.clboy.clkit.net.service.LanService;
import cn.clboy.clkit.net.vo.IpPortListVO;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.net.NetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 局域网服务impl
 *
 * @author clboy
 * @date 2024/04/22 10:17:31
 */
@Service
@RequiredArgsConstructor
public class LanServiceImpl implements LanService {

    @Override
    public List<String> getIpList() {
        return NetUtil.localIpv4s().stream().filter(ip -> !NetUtil.LOCAL_IP.equals(ip)).collect(Collectors.toList());
    }

    @Override
    public List<String> scanIp(String baseIp) {
        this.checkIsLocalIp(baseIp);
        List<String> ipList = Ipv4Util.list(baseIp + "/24", true);
        ipList.remove(baseIp);
        return ipList.parallelStream().filter(ip -> NetUtil.ping(ip, 30)).collect(Collectors.toList());
    }

    @Override
    public List<IpPortListVO> scanPort(ScanPortQuery query) {
        Assert.notNull(query.getMinPort(), "invalid minPort");
        Assert.notNull(query.getMaxPort(), "invalid minPort");
        int portRange = query.getMaxPort() - query.getMinPort();
        Assert.isTrue(portRange >= 0 && portRange < 1000, "端口范围不能超过1000");
        this.checkIsLocalIp(query.getBaseIp());
        List<String> reachableIpList = this.scanIp(query.getBaseIp());

        List<Integer> portList = IntStream.range(query.getMinPort(), query.getMaxPort() + 1)
                .boxed().collect(Collectors.toList());

        return reachableIpList.parallelStream().map(ip -> {
            IpPortListVO vo = new IpPortListVO();
            vo.setIp(ip);
            List<Integer> reachablePortList = portList.parallelStream().filter(port -> {
                try {
                    Socket socket = new Socket(ip, port);
                    socket.close();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }).collect(Collectors.toList());
            vo.setPorts(reachablePortList);
            return vo;
        }).filter(vo -> !CollectionUtils.isEmpty(vo.getPorts())).collect(Collectors.toList());
    }

    /**
     * 检查是本地IP
     *
     * @param ip IP
     */
    private void checkIsLocalIp(String ip) {
        Assert.hasText(ip, "invalid ip");
        Assert.isTrue(this.getIpList().contains(ip), ip + " 非本机ip");
    }

}
