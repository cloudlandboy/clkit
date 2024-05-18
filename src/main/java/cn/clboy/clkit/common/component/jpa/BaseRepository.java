package cn.clboy.clkit.common.component.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 基础资料档案库
 *
 * @author clboy
 * @date 2024/05/11 15:42:29
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    Optional<T> findOne(Consumer<LambdaPredicateBuilder<T>> consumer);

    List<T> findAll(Consumer<LambdaPredicateBuilder<T>> consumer);

    Page<T> findAll(Consumer<LambdaPredicateBuilder<T>> consumer, Pageable pageable);

    List<T> findAll(Consumer<LambdaPredicateBuilder<T>> consumer, Sort sort);

    boolean exists(Consumer<LambdaPredicateBuilder<T>> consumer);

    long count(Consumer<LambdaPredicateBuilder<T>> consumer);
}
