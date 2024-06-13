package cn.clboy.clkit.code.service.impl;

import cn.clboy.clkit.code.entity.Regexp;
import cn.clboy.clkit.code.repository.RegexpRepository;
import cn.clboy.clkit.code.service.RegexpService;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 正则服务实施
 *
 * @author clboy
 * @date 2024/06/03 17:28:13
 */
@Service
@RequiredArgsConstructor
public class RegexpServiceImpl extends AppDataHandlerCrudServiceImpl<Regexp, RegexpRepository> implements RegexpService {

    private final RestTemplate restTemplate;

    @Override
    public String downloadAnyRuleJs() {
        String url = "https://gitee.com/mirrors/any-rule/raw/master/packages/www/src/RULES.js";
        String content = restTemplate.getForObject(url, String.class);
        if (content == null) {
            return "export default []";
        }
        return content.replace("module.exports =", "export default ");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void importData(List<Regexp> dataList) {
        Map<String, Regexp> existsMap = this.getAll().stream()
                .collect(Collectors.toMap(Regexp::getName, Function.identity()));
        dataList.forEach(data -> {
            Regexp regexp = existsMap.get(data.getName());
            if (regexp == null) {
                this.save(data);
                return;
            }
            regexp.setDescription(data.getDescription());
            regexp.setRegex(data.getRegex());
            regexp.setExamples(data.getExamples());
            regexp.setCounterExamples(data.getCounterExamples());
            this.updateById(regexp);
        });

    }
}
