package cn.clboy.clkit.common.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常控制器
 *
 * @author clboy
 * @date 2024/04/24 10:57:33
 */
@Slf4j
//@RestController
@RestControllerAdvice
public class GlobalExceptionController extends AbstractErrorController {

    public GlobalExceptionController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }


    /**
     * 处理程序错误
     *
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ApiResult<Void> handlerError(HttpServletRequest request, HttpServletResponse response) {
        return ApiResult.of(super.getStatus(request));
    }

    /**
     * Exception处理
     *
     * @param ex ex
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<Object> exception(Exception ex) {
        log.error("未知错误", ex);
        return ApiResult.builder().msg(ex.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    /**
     * 非法参数异常
     *
     * @param ex ex
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> illegalArgumentException(IllegalArgumentException ex) {
        return ApiResult.builder().msg(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value()).build();
    }

    /**
     * 参数异常
     *
     * @param ex ex
     */
    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> servletException(ServletException ex) {
        return ApiResult.builder().msg(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value()).build();
    }

    /**
     * 参数绑定异常
     *
     * @param ex ex
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> bindException(BindException ex) {
        return ApiResult.builder().msg(ex.getAllErrors().get(0).getDefaultMessage())
                .code(HttpStatus.BAD_REQUEST.value()).build();
    }

}
