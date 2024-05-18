package cn.clboy.clkit.common.component.jpa;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 包装root
 *
 * @author clboy
 * @date 2024/05/11 15:10:59
 */
public class RootWrapper<X> implements Root<X> {

    private final Root root;

    public RootWrapper(Root root) {
        this.root = root;
    }

    /**
     * 获取
     *
     * @param func func
     */
    public <Y> Path<Y> get(Func1<X, ?> func) {
        return this.root.get(LambdaUtil.getFieldName(func));
    }

    @Override
    public EntityType<X> getModel() {
        return this.root.getModel();
    }

    @Override
    public Path<?> getParentPath() {
        return this.root.getParentPath();
    }

    @Override
    public <Y> Path<Y> get(SingularAttribute<? super X, Y> attribute) {
        return this.root.get(attribute);
    }

    @Override
    public <E, C extends Collection<E>> Expression<C> get(PluralAttribute<X, C, E> collection) {
        return this.root.get(collection);
    }

    @Override
    public <K, V, M extends Map<K, V>> Expression<M> get(MapAttribute<X, K, V> map) {
        return this.root.get(map);
    }

    @Override
    public Expression<Class<? extends X>> type() {
        return this.root.type();
    }

    @Override
    public <Y> Path<Y> get(String attributeName) {
        return this.root.get(attributeName);
    }

    @Override
    public Set<Join<X, ?>> getJoins() {
        return this.root.getJoins();
    }

    @Override
    public boolean isCorrelated() {
        return this.root.isCorrelated();
    }

    @Override
    public From<X, X> getCorrelationParent() {
        return this.root.getCorrelationParent();
    }

    @Override
    public <Y> Join<X, Y> join(SingularAttribute<? super X, Y> attribute) {
        return this.root.join(attribute);
    }

    @Override
    public <Y> Join<X, Y> join(SingularAttribute<? super X, Y> attribute, JoinType jt) {
        return this.root.join(attribute, jt);
    }

    @Override
    public <Y> CollectionJoin<X, Y> join(CollectionAttribute<? super X, Y> collection) {
        return this.root.join(collection);
    }

    @Override
    public <Y> SetJoin<X, Y> join(SetAttribute<? super X, Y> set) {
        return this.root.join(set);
    }

    @Override
    public <Y> ListJoin<X, Y> join(ListAttribute<? super X, Y> list) {
        return this.root.join(list);
    }

    @Override
    public <K, V> MapJoin<X, K, V> join(MapAttribute<? super X, K, V> map) {
        return this.root.join(map);
    }

    @Override
    public <Y> CollectionJoin<X, Y> join(CollectionAttribute<? super X, Y> collection, JoinType jt) {
        return this.root.join(collection, jt);
    }

    @Override
    public <Y> SetJoin<X, Y> join(SetAttribute<? super X, Y> set, JoinType jt) {
        return this.root.join(set, jt);
    }

    @Override
    public <Y> ListJoin<X, Y> join(ListAttribute<? super X, Y> list, JoinType jt) {
        return this.root.join(list, jt);
    }

    @Override
    public <K, V> MapJoin<X, K, V> join(MapAttribute<? super X, K, V> map, JoinType jt) {
        return this.root.join(map, jt);
    }

    @Override
    public <X1, Y> Join<X1, Y> join(String attributeName) {
        return this.root.join(attributeName);
    }

    @Override
    public <X1, Y> CollectionJoin<X1, Y> joinCollection(String attributeName) {
        return this.root.joinCollection(attributeName);
    }

    @Override
    public <X1, Y> SetJoin<X1, Y> joinSet(String attributeName) {
        return this.root.joinSet(attributeName);
    }

    @Override
    public <X1, Y> ListJoin<X1, Y> joinList(String attributeName) {
        return this.root.joinList(attributeName);
    }

    @Override
    public <X1, K, V> MapJoin<X1, K, V> joinMap(String attributeName) {
        return this.root.joinMap(attributeName);
    }

    @Override
    public <X1, Y> Join<X1, Y> join(String attributeName, JoinType jt) {
        return this.root.join(attributeName, jt);
    }

    @Override
    public <X1, Y> CollectionJoin<X1, Y> joinCollection(String attributeName, JoinType jt) {
        return this.root.joinCollection(attributeName, jt);
    }

    @Override
    public <X1, Y> SetJoin<X1, Y> joinSet(String attributeName, JoinType jt) {
        return this.root.joinSet(attributeName, jt);
    }

    @Override
    public <X1, Y> ListJoin<X1, Y> joinList(String attributeName, JoinType jt) {
        return this.root.joinList(attributeName, jt);
    }

    @Override
    public <X1, K, V> MapJoin<X1, K, V> joinMap(String attributeName, JoinType jt) {
        return this.root.joinMap(attributeName, jt);
    }

    @Override
    public Predicate isNull() {
        return this.root.isNull();
    }

    @Override
    public Predicate isNotNull() {
        return this.root.isNotNull();
    }

    @Override
    public Predicate in(Object... values) {
        return this.root.in(values);
    }

    @Override
    public Predicate in(Expression<?>... values) {
        return this.root.in(values);
    }

    @Override
    public Predicate in(Collection<?> values) {
        return this.root.in(values);
    }

    @Override
    public Predicate in(Expression<Collection<?>> values) {
        return this.root.in(values);
    }

    @Override
    public <X1> Expression<X1> as(Class<X1> type) {
        return this.root.as(type);
    }

    @Override
    public Set<Fetch<X, ?>> getFetches() {
        return this.root.getFetches();
    }

    @Override
    public <Y> Fetch<X, Y> fetch(SingularAttribute<? super X, Y> attribute) {
        return this.root.fetch(attribute);
    }

    @Override
    public <Y> Fetch<X, Y> fetch(SingularAttribute<? super X, Y> attribute, JoinType jt) {
        return this.root.fetch(attribute, jt);
    }

    @Override
    public <Y> Fetch<X, Y> fetch(PluralAttribute<? super X, ?, Y> attribute) {
        return this.root.fetch(attribute);
    }

    @Override
    public <Y> Fetch<X, Y> fetch(PluralAttribute<? super X, ?, Y> attribute, JoinType jt) {
        return this.root.fetch(attribute, jt);
    }

    @Override
    public <X1, Y> Fetch<X1, Y> fetch(String attributeName) {
        return this.root.fetch(attributeName);
    }

    @Override
    public <X1, Y> Fetch<X1, Y> fetch(String attributeName, JoinType jt) {
        return this.root.fetch(attributeName, jt);
    }

    @Override
    public Selection<X> alias(String name) {
        return this.root.alias(name);
    }

    @Override
    public boolean isCompoundSelection() {
        return this.root.isCompoundSelection();
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return this.root.getCompoundSelectionItems();
    }

    @Override
    public Class<? extends X> getJavaType() {
        return this.root.getJavaType();
    }

    @Override
    public String getAlias() {
        return this.root.getAlias();
    }
}