package cn.clboy.clkit.extension.filter;

import cn.clboy.clkit.common.util.AppUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展资源过滤器
 *
 * @author clboy
 * @date 2024/05/07 10:04:19
 */
@Slf4j
public class ExtensionResourceFilter implements Filter {
    public static final String PATH_PREFIX = "/extension/";
    public static final Pattern PATH_PATTERN = Pattern.compile("^" + PATH_PREFIX + "(\\d+)/(.*)");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (this.attemptServiceExtensionReq(req, res)) {
            return;
        }
        if (this.attemptRedirectExtensionReq(req, res)) {
            return;
        }
        chain.doFilter(request, response);

    }

    /**
     * 尝试服务扩展请求
     *
     * @param request 请求
     * @param res     res
     */
    private boolean attemptServiceExtensionReq(HttpServletRequest request, HttpServletResponse res) throws IOException {
        Matcher matcher = PATH_PATTERN.matcher(request.getServletPath());
        boolean isExtensionReq = matcher.matches();
        log.debug("path：{}, isExtensionResource: {}", request.getServletPath(), isExtensionReq);
        if (!isExtensionReq) {
            return false;
        }

        String mediaType = FileUtil.getMimeType(request.getServletPath());
        Long extensionId = Long.parseLong(matcher.group(1));
        String extensionFilePath = matcher.group(2);
        File extensionFile = AppUtils.getExtensionFile(extensionId, extensionFilePath);
        String contentEncoding = null;
        if (!extensionFile.exists()) {
            String acceptEncoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
            if (acceptEncoding.contains("gzip")) {
                extensionFile = AppUtils.getExtensionFile(extensionId, extensionFilePath + ".gz");
                contentEncoding = "gzip";
            }
        }
        if (extensionFile.exists()) {
            byte[] bytes = IoUtil.readBytes(Files.newInputStream(extensionFile.toPath()));
            res.setContentType(mediaType);
            if (contentEncoding != null) {
                res.setHeader(HttpHeaders.CONTENT_ENCODING, contentEncoding);
            }
            res.getOutputStream().write(bytes);
        } else {
            res.setStatus(HttpStatus.NOT_FOUND.value());
            res.getWriter().write(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return true;
    }

    /**
     * 尝试重定向扩展请求
     *
     * @param req 请求
     * @param res res
     */
    private boolean attemptRedirectExtensionReq(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //未携带referer请求头的无法处理，如img标签
        String referer = req.getHeader(HttpHeaders.REFERER);
        if (StringUtils.hasText(referer)) {
            Matcher matcher = PATH_PATTERN.matcher(URI.create(referer).getPath());
            if (matcher.matches()) {
                String path = PATH_PREFIX + matcher.group(1) + req.getServletPath();
                res.sendRedirect(path);
                log.debug("referer：{}, isExtensionResource: {}, redirectTo：{}", referer, true, path);
                return true;
            }
        }
        log.debug("referer：{}, isExtensionResource: {}", referer, false);
        return false;
    }
}
