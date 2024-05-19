package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.common.web.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * clKit身份验证失败处理程序
 *
 * @author clboy
 * @date 2024/05/19 13:45:10
 */
@Slf4j
@AllArgsConstructor
public class ClkitAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        //拦截错误响应
        int status = HttpStatus.UNAUTHORIZED.value();
        if (exception instanceof InternalAuthenticationServiceException) {
            log.error("认证出错了", exception);
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(ApiResult.builder()
                .msg(exception.getMessage()).code(status).build()));
    }
}
