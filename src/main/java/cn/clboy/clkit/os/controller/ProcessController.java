package cn.clboy.clkit.os.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.os.service.ProcessService;
import cn.clboy.clkit.os.vo.PidInfoVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 进程控制器
 *
 * @author clboy
 * @date 2024/04/22 11:33:31
 */
@RestController
@RequestMapping("os/process")
@RequiredArgsConstructor
@Tag(name = "进程管理")
public class ProcessController {

    private final ProcessService processService;


    /**
     * 获取信息
     *
     * @param type  类型
     * @param value 值
     */
    @GetMapping("info/{type}/{value}")
    @PreAuthorize("@authChecker.hasPermission('os_process_view')")
    public ApiResult<List<PidInfoVO>> getInfo(@PathVariable("type") String type, @PathVariable("value") String value) {
        return ApiResult.ok(processService.getInfo(type, value));
    }

}
