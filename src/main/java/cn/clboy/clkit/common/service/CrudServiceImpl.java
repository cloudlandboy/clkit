package cn.clboy.clkit.common.service;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * 数据库服务实施
 *
 * @author clboy
 * @date 2024/04/18 16:45:06
 */
public abstract class CrudServiceImpl<T, ID, R extends BaseRepository<T, ID>> implements CrudService<T, ID>, InitializingBean {

    protected R repository;

    @Getter
    private Class<T> modelClass;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T updateById(T entity) {
        return repository.save(entity);
    }

    @Override
    public T getById(ID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("资源不存在"));
    }

    @Override
    public List<T> getByIds(Collection<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.modelClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
}
