package cn.clboy.clkit.os.handler;

import cn.clboy.clkit.os.metadata.LanHostMetadata;
import cn.clboy.clkit.os.vo.PidInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 不支持操作系统处理程序
 *
 * @author clboy
 * @date 2024/04/22 15:25:14
 */
@Slf4j
@Component
@ConditionalOnMissingBean(OsHandler.class)
class UnsupportedOsHandler implements OsHandler {

    @Override
    public PidInfoVO findPidInfo(Integer pid) {
        throw new UnsupportedOperationException("不支持的操作系统");
    }

    @Override
    public PidInfoVO findPidInfoByPort(Integer port) {
        return this.findPidInfo(null);
    }

    @Override
    public List<PidInfoVO> findPidInfoByName(String name) {
        this.findPidInfo(null);
        return null;
    }

    @Override
    public void openUrl(String url) {
        log.error("不支持的操作系统,无法打开链接");
    }

    @Override
    public List<LanHostMetadata> getArpInfo(String assignedIp) {
        throw new UnsupportedOperationException("不支持的操作系统");
    }

}