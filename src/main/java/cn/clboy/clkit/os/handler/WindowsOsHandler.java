package cn.clboy.clkit.os.handler;

import cn.clboy.clkit.component.condition.ConditionalOnOs;
import cn.clboy.clkit.os.vo.PidInfoVO;
import cn.hutool.core.io.IoUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Windows操作系统处理程序
 *
 * @author clboy
 * @date 2024/04/22 15:43:00
 */
@Component
@ConditionalOnOs(names = "Windows")
public class WindowsOsHandler implements OsHandler {

    @Override
    @SneakyThrows
    public PidInfoVO findPidInfo(Integer pid) {
        ProcessBuilder ps = new ProcessBuilder("tasklist", "/NH", "/FO", "CSV", "/FI",
                String.format("\"PID eq %d\"", pid));
        Process process = ps.start();
        String output = IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        return this.parsePidInfo(output);
    }

    @Override
    @SneakyThrows
    public PidInfoVO findPidInfoByPort(Integer port) {
        ProcessBuilder ps = new ProcessBuilder("cmd", "/c",
                "netstat -ano | findstr :" + port);
        Process process = ps.start();
        String output = IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        String[] lines = output.split("\r\n");
        for (String line : lines) {
            String[] parts = line.trim().split("\\s");
            boolean isLocal = Arrays.stream(parts).anyMatch(p -> "LISTENING".equals(p.trim()));
            if (!isLocal) {
                continue;
            }
            String pidStr = parts[parts.length - 1];
            if (!StringUtils.hasText(pidStr)) {
                continue;
            }
            try {
                PidInfoVO pidInfo = this.findPidInfo(Integer.parseInt(pidStr));
                if (pidInfo != null) {
                    return pidInfo;
                }
            } catch (NumberFormatException ex) {
                //ignore
            }
        }
        return null;
    }

    @Override
    @SneakyThrows
    public List<PidInfoVO> findPidInfoByName(String name) {
        ProcessBuilder ps = new ProcessBuilder("cmd", "/c",
                "tasklist /NH /FO CSV | findstr " + name);
        Process process = ps.start();
        String output = IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        return Arrays.stream(output.split("\r\n")).filter(StringUtils::hasText)
                .map(this::parsePidInfo).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public void openUrl(String url) {
        ProcessBuilder ps = new ProcessBuilder("start", url);
        ps.start();
    }

    /**
     * 解析pid信息
     *
     * @param output 输出
     */
    private PidInfoVO parsePidInfo(String output) {
        if (!StringUtils.hasText(output)) {
            return null;
        }
        PidInfoVO pidInfo = new PidInfoVO();
        String[] parts = output.split("\",");
        pidInfo.setImageName(parts[0].replaceAll("\"", ""));
        pidInfo.setPid(Integer.parseInt(parts[1].replaceAll("\"", "")));
        pidInfo.setMemUsage(Long.parseLong(parts[4].replaceAll("[^0-9]", "")));
        return pidInfo;
    }
}
