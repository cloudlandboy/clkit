package cn.clboy.clkit.common.component.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 基础资料档案库
 *
 * @author clboy
 * @date 2024/05/11 15:42:29
 */
public class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    public Optional<T> findOne(Consumer<LambdaPredicateBuilder<T>> consumer) {
        return super.findOne((Specification<T>) (root, query, criteriaBuilder) -> {
            LambdaPredicateBuilder<T> builder = new LambdaPredicateBuilder<>(root, query, criteriaBuilder);
            consumer.accept(builder);
            return builder.build();
        });
    }

    @Override
    public List<T> findAll(Consumer<LambdaPredicateBuilder<T>> consumer) {
        return super.findAll((Specification<T>) (root, query, criteriaBuilder) -> {
            LambdaPredicateBuilder<T> builder = new LambdaPredicateBuilder<>(root, query, criteriaBuilder);
            consumer.accept(builder);
            return builder.build();
        });
    }

    @Override
    public Page<T> findAll(Consumer<LambdaPredicateBuilder<T>> consumer, Pageable pageable) {
        return super.findAll((Specification<T>) (root, query, criteriaBuilder) -> {
            LambdaPredicateBuilder<T> builder = new LambdaPredicateBuilder<>(root, query, criteriaBuilder);
            consumer.accept(builder);
            return builder.build();
        }, pageable);
    }

    @Override
    public List<T> findAll(Consumer<LambdaPredicateBuilder<T>> consumer, Sort sort) {
        return super.findAll((Specification<T>) (root, query, criteriaBuilder) -> {
            LambdaPredicateBuilder<T> builder = new LambdaPredicateBuilder<>(root, query, criteriaBuilder);
            consumer.accept(builder);
            return builder.build();
        }, sort);
    }

    @Override
    public boolean exists(Consumer<LambdaPredicateBuilder<T>> consumer) {
        return super.exists((Specification<T>) (root, query, criteriaBuilder) -> {
            LambdaPredicateBuilder<T> builder = new LambdaPredicateBuilder<>(root, query, criteriaBuilder);
            consumer.accept(builder);
            return builder.build();
        });
    }

    @Override
    public long count(Consumer<LambdaPredicateBuilder<T>> consumer) {
        return super.count((Specification<T>) (root, query, criteriaBuilder) -> {
            LambdaPredicateBuilder<T> builder = new LambdaPredicateBuilder<>(root, query, criteriaBuilder);
            consumer.accept(builder);
            return builder.build();
        });
    }
}
