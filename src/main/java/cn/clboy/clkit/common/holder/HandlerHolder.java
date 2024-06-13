package cn.clboy.clkit.common.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手柄支架
 *
 * @author clboy
 * @date 2024/04/18 18:00:25
 */
public class HandlerHolder<K, H> {
    private final Map<K, H> handerMap = new HashMap<>();

    public H getHandler(K k) {
        return handerMap.get(k);
    }

    public void addHandler(K k, H handler) {
        handerMap.put(k, handler);
    }

    public List<H> getHandlers() {
        return new ArrayList<>(handerMap.values());
    }

}
