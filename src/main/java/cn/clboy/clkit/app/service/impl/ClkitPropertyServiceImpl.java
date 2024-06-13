package cn.clboy.clkit.app.service.impl;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.app.handler.AppDataHandler;
import cn.clboy.clkit.app.repository.ClkitPropertyRepository;
import cn.clboy.clkit.app.service.ClkitPropertyService;
import cn.clboy.clkit.common.constants.enums.PropertyEnum;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * clKit配置服务impl
 *
 * @author clboy
 * @date 2024/05/17 15:20:51
 */
@Slf4j
@Service
public class ClkitPropertyServiceImpl extends AppDataHandlerCrudServiceImpl<ClkitProperty, ClkitPropertyRepository>
        implements ClkitPropertyService, AppDataHandler<ClkitProperty>, ApplicationRunner {

    @Override
    public ClkitProperty getByKey(String key) {
        Optional<ClkitProperty> optional = this.repository.findOne(builder -> {
            builder.equal(ClkitProperty::getPropKey, key);
        });
        return optional.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClkitProperty updateValueById(Long id, String value) {
        ClkitProperty property = getById(id);
        Assert.isTrue(Boolean.TRUE.equals(property.getEditable()), "该参数禁止编辑");
        property.setPropValue(value);
        this.updateById(property);
        return property;
    }

    @Override
    public synchronized void importData(List<ClkitProperty> dataList) {
        //TODO
        log.info("暂不支持");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) throws Exception {
        //初始化参数
        List<String> existsKey = this.getAll().stream().map(ClkitProperty::getPropKey).collect(Collectors.toList());

        for (PropertyEnum pe : PropertyEnum.values()) {
            if (existsKey.contains(pe.getValue())) {
                continue;
            }
            ClkitProperty property = new ClkitProperty();
            property.setName(pe.getLabel());
            property.setPropKey(pe.getValue());
            property.setPropValue(pe.getInitValue());
            property.setDescription(pe.getDesc());
            property.setInternal(true);
            property.setEditable(pe.isEditable());
            this.save(property);
        }
    }
}
