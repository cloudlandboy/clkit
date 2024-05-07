package cn.clboy.clkit.net.service.impl;

import cn.clboy.clkit.net.service.InternetService;
import cn.clboy.clkit.net.service.LanService;
import cn.clboy.clkit.net.vo.InternetInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 互联网服务impl
 *
 * @author clboy
 * @date 2024/05/04 10:28:51
 */
@Service
@RequiredArgsConstructor
public class InternetServiceImpl implements InternetService {

    private final LanService lanService;
    private final RestTemplate restTemplate;

    @Override
    public InternetInfoVO getInfo() {
        InternetInfoVO vo = new InternetInfoVO();
        vo.setLanIpList(lanService.getIpList());
        try {
            String ip = restTemplate.getForObject("https://api.ipify.org", String.class);
            vo.setInternetIp(ip);
        } catch (Exception ex) {
            //ignore
        }
        return vo;
    }
}
