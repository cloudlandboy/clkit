package cn.clboy.clkit.net.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.net.query.ScanPortQuery;
import cn.clboy.clkit.net.service.LanService;
import cn.clboy.clkit.net.vo.IpPortListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 局域网控制器
 *
 * @author clboy
 * @date 2024/04/22 10:12:39
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("net/lan")
@PreAuthorize("@authChecker.hasPermission('net_lan_view')")
public class LanController {

    private final LanService lanService;

    /**
     * 获取IP列表
     */
    @GetMapping("ip_list")
    public ApiResult<List<String>> getIpList() {
        return ApiResult.ok(lanService.getIpList());
    }

    /**
     * 扫描ip
     */
    @GetMapping("scan_ip")
    public ApiResult<List<String>> scanIp(String baseIp) {
        return ApiResult.ok(lanService.scanIp(baseIp));
    }

    /**
     * 扫描端口
     *
     * @param query 查询
     */
    @GetMapping("scan_port")
    public ApiResult<List<IpPortListVO>> scanPort(ScanPortQuery query) {
        return ApiResult.ok(lanService.scanPort(query));
    }

}
