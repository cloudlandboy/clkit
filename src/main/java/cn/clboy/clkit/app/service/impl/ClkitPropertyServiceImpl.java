package cn.clboy.clkit.app.service.impl;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.app.handler.AppDataHandler;
import cn.clboy.clkit.app.repository.ClkitPropertyRepository;
import cn.clboy.clkit.app.service.ClkitPropertyService;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * clKit配置服务impl
 *
 * @author clboy
 * @date 2024/05/17 15:20:51
 */
@Slf4j
@Service
public class ClkitPropertyServiceImpl extends AppDataHandlerCrudServiceImpl<ClkitProperty, ClkitPropertyRepository>
        implements ClkitPropertyService, AppDataHandler<ClkitProperty> {

    @Override
    public ClkitProperty getByKey(String key) {
        Optional<ClkitProperty> optional = this.repository.findOne(builder -> {
            builder.equal(ClkitProperty::getPropKey, key);
        });
        return optional.orElse(null);
    }

    @Override
    public synchronized void importData(List<ClkitProperty> dataList) {
        //TODO
        log.info("暂不支持");
    }
}
