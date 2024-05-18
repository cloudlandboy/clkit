package cn.clboy.clkit.common.component.jpa;

import cn.hutool.core.lang.func.Func1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import javax.persistence.criteria.Predicate.BooleanOperator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Lambda断言生成器
 *
 * @author clboy
 * @date 2024/05/11 17:35:57
 */
public class LambdaPredicateBuilder<T> {
    private final RootWrapper<T> root;
    private final CriteriaQuery<?> query;
    private final CriteriaBuilder criteriaBuilder;
    private BooleanOperator nextOperator = BooleanOperator.AND;
    private final List<Order> orders = new ArrayList<>();
    private final List<ChainEntry> chain = new LinkedList<>();

    public LambdaPredicateBuilder(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        this.root = new RootWrapper<>(root);
        this.query = query;
        this.criteriaBuilder = criteriaBuilder;
    }

    /**
     * 和
     *
     * @param consumer 消费者
     */
    public LambdaPredicateBuilder<T> and(Consumer<LambdaPredicateBuilder<T>> consumer) {
        LambdaPredicateBuilder<T> subBuilder = this.copy();
        consumer.accept(subBuilder);
        Predicate predicate = subBuilder.build();
        if (predicate != null) {
            this.addPredicate(BooleanOperator.AND, predicate);
        }
        return this;
    }

    /**
     * 或
     *
     * @param consumer 消费者
     */
    public LambdaPredicateBuilder<T> or(Consumer<LambdaPredicateBuilder<T>> consumer) {
        LambdaPredicateBuilder<T> subBuilder = this.copy();
        consumer.accept(subBuilder);
        Predicate predicate = subBuilder.build();
        if (predicate != null) {
            this.addPredicate(BooleanOperator.OR, predicate);
        }
        return this;
    }

    /**
     * 或
     */
    public LambdaPredicateBuilder<T> or() {
        this.nextOperator = BooleanOperator.OR;
        return this;
    }


    public LambdaPredicateBuilder<T> asc(Func1<T, ?> func) {
        orders.add(criteriaBuilder.asc(root.get(func)));
        return this;
    }

    public LambdaPredicateBuilder<T> desc(Func1<T, ?> func) {
        orders.add(criteriaBuilder.desc(root.get(func)));
        return this;
    }

    public LambdaPredicateBuilder<T> isTrue(Func1<T, Boolean> func) {
        this.addPredicate(criteriaBuilder.isTrue(root.get(func)));
        return this;
    }

    public LambdaPredicateBuilder<T> isFalse(Func1<T, Boolean> func) {
        this.addPredicate(criteriaBuilder.isFalse(root.get(func)));
        return this;
    }

    public LambdaPredicateBuilder<T> isNull(Func1<T, ?> func) {
        this.addPredicate(criteriaBuilder.isNull(root.get(func)));
        return this;
    }

    public LambdaPredicateBuilder<T> isNotNull(Func1<T, ?> func) {
        this.addPredicate(criteriaBuilder.isNotNull(root.get(func)));
        return this;
    }

    public LambdaPredicateBuilder<T> equal(Func1<T, ?> x, Func1<T, ?> y) {
        this.addPredicate(criteriaBuilder.equal(root.get(x), root.get(y)));
        return this;
    }

    public LambdaPredicateBuilder<T> equal(Func1<T, ?> x, Object y) {
        this.addPredicate(criteriaBuilder.equal(root.get(x), y));
        return this;
    }

    public LambdaPredicateBuilder<T> notEqual(Func1<T, ?> x, Func1<T, ?> y) {
        this.addPredicate(criteriaBuilder.notEqual(root.get(x), root.get(y)));
        return this;
    }

    public LambdaPredicateBuilder<T> notEqual(Func1<T, ?> x, Object y) {
        this.addPredicate(criteriaBuilder.notEqual(root.get(x), y));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> greaterThan(Func1<T, ? extends Y> x, Func1<T, ? extends Y> y) {
        this.addPredicate(criteriaBuilder.greaterThan(root.get(x), root.get(y)));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> greaterThan(Func1<T, ? extends Y> x, Y y) {
        this.addPredicate(criteriaBuilder.greaterThan(root.get(x), y));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> greaterThanOrEqualTo(Func1<T, ? extends Y> x, Func1<T, ? extends Y> y) {
        this.addPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(x), root.get(y)));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> greaterThanOrEqualTo(Func1<T, ? extends Y> x, Y y) {
        this.addPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(x), y));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> lessThan(Func1<T, ? extends Y> x, Func1<T, ? extends Y> y) {
        this.addPredicate(criteriaBuilder.lessThan(root.get(x), root.get(y)));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> lessThan(Func1<T, ? extends Y> x, Y y) {
        this.addPredicate(criteriaBuilder.lessThan(root.get(x), y));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> lessThanOrEqualTo(Func1<T, ? extends Y> x, Func1<T, ? extends Y> y) {
        this.addPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(x), root.get(y)));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> lessThanOrEqualTo(Func1<T, ? extends Y> x, Y y) {
        this.addPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(x), y));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> between(Func1<T, ? extends Y> v, Func1<T, ? extends Y> x, Func1<T, ? extends Y> y) {
        this.addPredicate(criteriaBuilder.between(root.get(v), root.get(x), root.get(y)));
        return this;
    }

    public <Y extends Comparable<? super Y>> LambdaPredicateBuilder<T> between(Func1<T, ? extends Y> v, Y x, Y y) {
        this.addPredicate(criteriaBuilder.between(root.get(v), x, y));
        return this;
    }

    public LambdaPredicateBuilder<T> gt(Func1<T, ? extends Number> x, Func1<T, ? extends Number> y) {
        this.addPredicate(criteriaBuilder.gt(root.get(x), root.get(y)));
        return this;
    }

    public LambdaPredicateBuilder<T> gt(Func1<T, ? extends Number> x, Number y) {
        this.addPredicate(criteriaBuilder.gt(root.get(x), y));
        return this;
    }

    public LambdaPredicateBuilder<T> ge(Func1<T, ? extends Number> x, Func1<T, ? extends Number> y) {
        this.addPredicate(criteriaBuilder.ge(root.get(x), root.get(y)));
        return this;
    }

    public LambdaPredicateBuilder<T> ge(Func1<T, ? extends Number> x, Number y) {
        this.addPredicate(criteriaBuilder.ge(root.get(x), y));
        return this;
    }

    public LambdaPredicateBuilder<T> lt(Func1<T, ? extends Number> x, Func1<T, ? extends Number> y) {
        this.addPredicate(criteriaBuilder.lt(root.get(x), root.get(y)));
        return this;
    }

    public LambdaPredicateBuilder<T> lt(Func1<T, ? extends Number> x, Number y) {
        this.addPredicate(criteriaBuilder.lt(root.get(x), y));
        return this;
    }

    public LambdaPredicateBuilder<T> le(Func1<T, ? extends Number> x, Func1<T, ? extends Number> y) {
        this.addPredicate(criteriaBuilder.le(root.get(x), root.get(y)));
        return this;
    }

    public LambdaPredicateBuilder<T> le(Func1<T, ? extends Number> x, Number y) {
        this.addPredicate(criteriaBuilder.le(root.get(x), y));
        return this;
    }

    public LambdaPredicateBuilder<T> like(Func1<T, String> x, String pattern) {
        this.addPredicate(criteriaBuilder.like(root.get(x), pattern));
        return this;
    }

    public LambdaPredicateBuilder<T> notLike(Func1<T, String> x, String pattern) {
        this.addPredicate(criteriaBuilder.notLike(root.get(x), pattern));
        return this;
    }

    public Predicate build() {
        this.query.orderBy(this.orders);
        if (CollectionUtils.isEmpty(this.chain)) {
            return null;
        }
        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        for (ChainEntry chainEntry : this.chain) {
            if (chainEntry.operator == BooleanOperator.OR) {
                this.merge(this.criteriaBuilder, andPredicates, BooleanOperator.AND).ifPresent(orPredicates::add);
                andPredicates.clear();
            }
            andPredicates.add(chainEntry.getPredicate());
        }
        this.merge(this.criteriaBuilder, andPredicates, BooleanOperator.AND).ifPresent(orPredicates::add);

        return this.merge(this.criteriaBuilder, orPredicates, BooleanOperator.OR).orElse(null);
    }


    /**
     * 合并Predicate
     *
     * @param builder    builder
     * @param predicates 谓词
     * @param operator   操作者
     */
    private Optional<Predicate> merge(CriteriaBuilder builder, List<Predicate> predicates, BooleanOperator operator) {
        if (CollectionUtils.isEmpty(predicates)) {
            return Optional.empty();
        }
        if (predicates.size() == 1) {
            return Optional.of(predicates.get(0));
        }

        Predicate[] predicateArray = predicates.toArray(predicates.toArray(new Predicate[0]));
        if (operator == BooleanOperator.AND) {
            return Optional.of(builder.and(predicateArray));
        }
        return Optional.of(builder.or(predicateArray));
    }

    /**
     * 添加断言
     *
     * @param predicate 谓词
     */
    private void addPredicate(Predicate predicate) {
        this.addPredicate(this.nextOperator, predicate);
    }

    /**
     * 添加断言
     *
     * @param operator  操作者
     * @param predicate 谓词
     */
    private void addPredicate(BooleanOperator operator, Predicate predicate) {
        this.chain.add(new ChainEntry(operator, predicate));
        //重置为and
        this.nextOperator = BooleanOperator.AND;
    }

    /**
     * 副本
     */
    private LambdaPredicateBuilder<T> copy() {
        return new LambdaPredicateBuilder<>(this.root, this.query, this.criteriaBuilder);
    }


    /**
     * 链条entry
     *
     * @author clboy
     * @date 2024/05/13 10:44:25
     */
    @Getter
    @AllArgsConstructor
    public static class ChainEntry {
        private final BooleanOperator operator;
        private final Predicate predicate;
    }
}
