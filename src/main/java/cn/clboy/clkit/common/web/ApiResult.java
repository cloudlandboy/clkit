package cn.clboy.clkit.common.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API结果
 *
 * @author clboy
 * @date 2024/02/29 17:09:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {

    public static final Integer SUCCESS_CODE = 0;
    public static final Integer ERROR_CODE = 1;

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public ApiResult(HttpStatus status, T data) {
        this(status.value(), status.getReasonPhrase(), data);
    }

    public ApiResult(HttpStatus status, String msg, T data) {
        this.code = status.value();
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResult<T> ok(String msg, T data) {
        return new ApiResult<>(SUCCESS_CODE, msg, data);
    }

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(SUCCESS_CODE, "success", data);
    }

    public static ApiResult<Void> ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> error(String msg, T data) {
        return new ApiResult<>(ERROR_CODE, msg, data);
    }

    public static <T> ApiResult<T> error(T data) {
        return new ApiResult<>(ERROR_CODE, "error", data);
    }

    public static ApiResult<Void> error() {
        return error(null);
    }

    public static ApiResult<Void> of(HttpStatus status) {
        return new ApiResult<>(status, null);
    }

}
