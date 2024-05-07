package cn.clboy.clkit.net.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.net.service.InternetService;
import cn.clboy.clkit.net.vo.InternetInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 互联网控制器
 *
 * @author clboy
 * @date 2024/05/04 09:33:45
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("net/internet")
public class InternetController {

    public final InternetService internetService;

    /**
     * 获取信息
     */
    @GetMapping("info")
    public ApiResult<InternetInfoVO> getInfo() {
        return ApiResult.ok(internetService.getInfo());
    }


}
