package cn.clboy.clkit.common.service;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 数据库服务实施
 *
 * @author clboy
 * @date 2024/04/18 16:45:06
 */
public class CrudServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements CrudService<T, ID>, InitializingBean {

    protected R repository;

    @Getter
    private Class<T> modelClass;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    @Override
    public T save(T dto) {
        return repository.save(dto);
    }

    @Override
    public T updateById(T dto) {
        return repository.save(dto);
    }

    @Override
    public T getById(ID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("资源不存在"));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public void removeById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.modelClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
}
