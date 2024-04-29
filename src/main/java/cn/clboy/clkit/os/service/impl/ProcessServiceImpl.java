package cn.clboy.clkit.os.service.impl;

import cn.clboy.clkit.os.handler.OsHandler;
import cn.clboy.clkit.os.service.ProcessService;
import cn.clboy.clkit.os.vo.PidInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 进程服务impl
 *
 * @author clboy
 * @date 2024/04/22 11:35:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final OsHandler osHandler;

    @Override
    public List<PidInfoVO> getInfo(String type, String value) {
        if ("port".equals(type)) {
            return Optional.ofNullable(this.findInfoByPort(Integer.parseInt(value)))
                    .map(Collections::singletonList).orElse(Collections.emptyList());
        } else if ("pid".equals(type)) {
            return Optional.ofNullable(this.findInfoByPid(Integer.parseInt(value)))
                    .map(Collections::singletonList).orElse(Collections.emptyList());
        } else if ("name".equals(type)) {
            return this.findInfoByName(value);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }
    }

    /**
     * 按名称查找信息
     *
     * @param name 名称
     */
    private List<PidInfoVO> findInfoByName(String name) {
        return osHandler.findPidInfoByName(name);
    }

    /**
     * 通过端口查找信息
     *
     * @param port 端口
     */
    private PidInfoVO findInfoByPort(Integer port) {
        return osHandler.findPidInfoByPort(port);
    }

    /**
     * 通过pid查找信息
     *
     * @param pid PID
     */
    private PidInfoVO findInfoByPid(Integer pid) {
        return osHandler.findPidInfo(pid);
    }

}
