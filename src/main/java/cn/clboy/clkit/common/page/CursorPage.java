package cn.clboy.clkit.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 光标分页
 *
 * @author clboy
 * @date 2024/03/29 15:53:18
 */
@Data
@Schema(name = "游标分页")
public abstract class CursorPage<T> {

    public static final Integer MAX_SIZE = 50;

    /**
     * 游标
     */
    @Schema(name = "游标")
    protected Object cursor;

    /**
     * 排序字段
     */
    @Schema(name = "排序字段")
    protected String orderBy;

    /**
     * 排序方式
     */
    @Schema(name = "排序方式")
    protected String orderDirection;

    /**
     * 显示条数
     */
    @Schema(name = "显示条数")
    protected Integer size = 20;

    /**
     * 记录
     */
    @Schema(name = "显示条数")
    protected List<T> records = Collections.emptyList();

    public void setSize(Integer size) {
        if (size == null) {
            return;
        }
        this.size = Math.min(size, MAX_SIZE);
    }

    /**
     * 是否有下一页
     */
    @Schema(name = "是否有下一页")
    public boolean getHasNext() {
        return records.size() == size;
    }
}
