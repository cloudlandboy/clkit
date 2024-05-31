package cn.clboy.clkit.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局异常控制器
 *
 * @author clboy
 * @date 2024/04/24 10:57:33
 */
@Slf4j
@RestController
@RestControllerAdvice
public class GlobalExceptionController extends AbstractErrorController {
    private final ObjectMapper objectMapper;

    public GlobalExceptionController(ObjectMapper objectMapper, ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.objectMapper = objectMapper;
    }


    /**
     * 处理程序错误
     *
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping("${server.error.path:${error.path:/error}}")
    public void handlerError(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpStatus status = super.getStatus(request);
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        String requestPath = (String) errorAttributes.get("path");
        if (status == HttpStatus.NOT_FOUND && !requestPath.startsWith("/api")) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(ApiResult.of(status)));
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
        ObjectError error = ex.getAllErrors().get(0);
        String message = error.getDefaultMessage();
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            if (message != null && !message.contains(field)) {
                message = field + message;
            }
        }
        return ApiResult.builder().msg(message).code(HttpStatus.BAD_REQUEST.value()).build();
    }

    /**
     * 访问拒绝异常
     *
     * @param ex ex
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<Object> accessDeniedException(AccessDeniedException ex) {
        return ApiResult.builder().msg(ex.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value()).build();
    }

}
