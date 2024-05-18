package cn.clboy.clkit.common.service;

import cn.clboy.clkit.app.handler.AppDataHandler;
import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应用程序数据处理程序crud服务实施
 *
 * @author clboy
 * @date 2024/04/28 17:44:38
 */
public class AppDataHandlerCrudServiceImpl<T, R extends BaseRepository<T, Long>> extends CrudServiceImpl<T, Long, R>
        implements AppDataHandler<T> {

    private String moduleName;

    @Override
    public String getModuleName() {
        return this.moduleName;
    }

    @Override
    public List<T> exportData(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return this.getAll();
        }
        return this.repository.findAllById(ids);
    }

    @Override
    public synchronized void importData(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (BaseEntity.class.isAssignableFrom(this.getModelClass())) {
            //清空id，避免更新
            dataList.forEach(data -> ((BaseEntity) data).setId(null));
        }

        if (IUniqueNameEntity.class.isAssignableFrom(this.getModelClass())) {
            //唯一名称
            Map<String, Long> nameCount = this.getAll().stream()
                    .collect(Collectors.groupingBy(e -> ((IUniqueNameEntity) e).getName(), Collectors.counting()));
            dataList.forEach(data -> {
                IUniqueNameEntity entity = (IUniqueNameEntity) data;
                Long count = nameCount.get(entity.getName());
                if (count == null) {
                    nameCount.put(entity.getName(), 1L);
                    return;
                }
                String autoName;
                do {
                    count++;
                    autoName = entity.getName() + "_" + count;
                } while (nameCount.containsKey(autoName));
                entity.setName(autoName);
                nameCount.put(autoName, 1L);
                nameCount.put(entity.getName(), count);
            });
        }

        this.repository.saveAll(dataList);
    }

    @Override
    public Class<T> getExportModelClass() {
        return this.getModelClass();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.moduleName = StrUtil.lowerFirst(this.getModelClass().getSimpleName());
        AppDataHandler.HOLDER.addHandler(this.getModuleName(), this);
    }
}
