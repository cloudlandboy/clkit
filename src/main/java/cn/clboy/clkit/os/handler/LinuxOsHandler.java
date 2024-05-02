package cn.clboy.clkit.os.handler;

import cn.clboy.clkit.common.component.condition.ConditionalOnOs;
import cn.clboy.clkit.os.vo.PidInfoVO;
import cn.hutool.core.io.IoUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Linux操作系统处理程序
 *
 * @author clboy
 * @date 2024/04/22 14:07:16
 */
@Component
@ConditionalOnOs(names = {"Linux", "LINUX"})
public class LinuxOsHandler extends AbstractOsHandler {

    @Override
    @SneakyThrows
    public PidInfoVO findPidInfo(Integer pid) {
        ProcessBuilder ps = new ProcessBuilder("ps", "-h", "-o", "pid,rss,comm", "-p", pid.toString());
        Process process = ps.start();
        String output = IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        return this.parsePidInfo(output);
    }

    @Override
    @SneakyThrows
    public PidInfoVO findPidInfoByPort(Integer port) {
        ProcessBuilder ps = new ProcessBuilder("lsof", "-i", ":" + port);
        Process process = ps.start();
        String output = IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        Optional<String> optional = Arrays.stream(output.trim().split("\n"))
                .filter(line -> line.indexOf("(LISTEN)") > 0)
                .findFirst();
        if (!optional.isPresent()) {
            return null;
        }

        Pattern pattern = Pattern.compile("\\s{4}(\\d+)\\s+");
        Matcher matcher = pattern.matcher(optional.get());
        if (matcher.find()) {
            int pid = Integer.parseInt(matcher.group().trim());
            return this.findPidInfo(pid);
        }

        return null;
    }

    @Override
    @SneakyThrows
    public List<PidInfoVO> findPidInfoByName(String name) {
        ProcessBuilder ps = new ProcessBuilder("bash", "-c",
                "ps -a -x -h -o pid,rss,comm | grep -i \"" + name + "\"");
        Process process = ps.start();
        String output = IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        return Arrays.stream(output.trim().split("\n"))
                .map(this::parsePidInfo).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public void openUrl(String url) {
        ProcessBuilder ps = new ProcessBuilder("open", url);
        ps.start();
    }


    /**
     * 解析pid信息
     *
     * @param text 文本
     */
    private PidInfoVO parsePidInfo(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        String[] parts = text.trim().split("\\s+");
        PidInfoVO pidInfo = new PidInfoVO();
        pidInfo.setPid(Integer.parseInt(parts[0]));
        pidInfo.setMemUsage(Long.parseLong(parts[1]));
        pidInfo.setImageName(parts[2]);
        return pidInfo;
    }

}
