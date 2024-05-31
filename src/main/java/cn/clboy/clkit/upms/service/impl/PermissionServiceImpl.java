package cn.clboy.clkit.upms.service.impl;

import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.constants.PermissionConstant;
import cn.clboy.clkit.common.service.CrudServiceImpl;
import cn.clboy.clkit.common.util.AppUtils;
import cn.clboy.clkit.upms.entity.Permission;
import cn.clboy.clkit.upms.repository.PermissionRepository;
import cn.clboy.clkit.upms.service.PermissionService;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.extra.spring.SpringUtil;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 权限service impl
 *
 * @author clboy
 * @date 2024/05/20 15:35:31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends CrudServiceImpl<Permission, Long, PermissionRepository>
        implements PermissionService, ApplicationRunner {

    private final RestTemplate restTemplate;

    @Override
    @SneakyThrows
    public String getPermissionJsConstDeclare() {
        List<String> statement = new ArrayList<>();
        Properties properties = new Properties();
        properties.load(new ClassPathResource("permission.properties").getInputStream());
        List<String> codeList = properties.keySet().stream().map(Objects::toString).sorted((a, b) -> {
            if (a.startsWith(ClkitConstant.ROLE_CODE_PREFIX)) {
                return -1;
            }
            return a.compareTo(b);
        }).collect(Collectors.toList());
        for (String code : codeList) {
            StringJoiner joiner = new StringJoiner("<br/>");
            joiner.add("/**");
            joiner.add(" * " + properties.get(code));
            joiner.add(" */");
            joiner.add(String.format("export const %s = \"%s\";", code.toUpperCase(), code));
            statement.add(joiner.toString());
        }
        return String.join("<br/><br/>", statement);
    }

    @Override
    public List<Tree<Long>> getTree() {
        List<Permission> permissionList = this.repository.findAll();
        List<Tree<Long>> treeList = new ArrayList<>();
        Map<String, Tree<Long>> snMap = new HashMap<>();
        for (Permission permission : permissionList) {
            String[] standardName = permission.getName().split("-");
            StringBuilder parentBuilder = null;
            for (int i = 0; i < standardName.length; i++) {
                String sn = standardName[i];
                if (parentBuilder == null) {
                    parentBuilder = new StringBuilder(sn);
                    if (snMap.containsKey(sn)) {
                        continue;
                    }
                    Tree<Long> tree = new Tree<>();
                    tree.putExtra("path", sn);
                    tree.setName(sn);
                    tree.setChildren(new ArrayList<>());
                    snMap.put(sn, tree);
                    treeList.add(tree);
                    continue;
                }
                String parentUnionKey = parentBuilder.toString();
                String path = parentBuilder.append("/").append(sn).toString();
                if (snMap.containsKey(path)) {
                    continue;
                }
                Tree<Long> parent = snMap.get(parentUnionKey);
                Tree<Long> tree = new Tree<>();
                tree.putExtra("path", path);
                tree.setName(sn);
                if (i == standardName.length - 1) {
                    tree.setId(permission.getId());
                    tree.putExtra("code", permission.getCode());
                } else {
                    tree.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(tree);
                parent.getChildren().sort((n, m) -> {
                    String nName = n.getName().toString();
                    String mName = m.getName().toString();
                    int lengthDiff = nName.length() - mName.length();
                    return lengthDiff == 0 ? nName.compareTo(mName) : lengthDiff;

                });
                snMap.put(path, tree);
            }
        }
        treeList.sort(Comparator.comparingInt(n -> n.getChildren().size()));
        return treeList;
    }

    /**
     * 1.自动往PermissionConstant.java中添加常量 ,注释由开发者自行更改
     * 2.写入resources/permission.properties
     * 3.与数据库比对，插入
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<File> optionalJavaFile = AppUtils.getSrcJavaFile(PermissionConstant.class);
        //开发模式
        boolean isDevEnv = optionalJavaFile.isPresent();
        log.info("是否是开发环境: {}", isDevEnv);
        List<String> permissions = this.collectPermissions();
        Properties properties = new Properties();
        if (isDevEnv) {
            File javaFile = optionalJavaFile.get();
            CompilationUnit unit = StaticJavaParser.parse(javaFile);
            ClassOrInterfaceDeclaration classDec = unit.getInterfaceByName(PermissionConstant.class.getSimpleName())
                    .orElseThrow(RuntimeException::new);
            List<String> newAddList = new ArrayList<>();
            for (String permission : permissions) {
                String fieldName = permission.toUpperCase();
                classDec.getFieldByName(fieldName).orElseGet(() -> {
                    FieldDeclaration fieldDec = classDec.addFieldWithInitializer(String.class,
                            fieldName, new StringLiteralExpr(permission));
                    fieldDec.setJavadocComment(permission);
                    newAddList.add(permission);
                    return fieldDec;
                });
            }
            if (!newAddList.isEmpty()) {
                IoUtil.write(Files.newOutputStream(javaFile.toPath()), StandardCharsets.UTF_8, true, unit.toString());
                //凡是本次运行存在新添加的，仅打印错误日志，提醒开发者修改注释后重启再入库
                for (int i = 0; i < 3; i++) {
                    log.error("重要事情说三遍：{} 中新添加了 {} 个权限定义",
                            PermissionConstant.class.getSimpleName(), newAddList.size());
                }
            }

            for (FieldDeclaration field : classDec.getFields()) {
                String code = field.getVariable(0).getInitializer()
                        .orElseThrow(RuntimeException::new).asStringLiteralExpr().getValue();
                JavadocComment comment = field.getJavadocComment().orElseThrow(RuntimeException::new);
                properties.put(code, comment.parse().getDescription().toText());
            }

            File resourceFile = AppUtils.getSrcResourceFile("permission.properties");
            //写入文件
            properties.store(Files.newOutputStream(resourceFile.toPath()), null);
        } else {
            properties.load(new ClassPathResource("permission.properties").getInputStream());
        }

        Map<String, Permission> existsMap = this.getAll().stream()
                .collect(Collectors.toMap(Permission::getCode, Function.identity()));
        properties.forEach((code, name) -> {
            if (code.toString().startsWith(ClkitConstant.ROLE_CODE_PREFIX)) {
                return;
            }
            Permission permission = existsMap.remove(code.toString());
            if (permission == null) {
                permission = new Permission();
                permission.setName(name.toString());
                permission.setCode(code.toString());
                this.save(permission);
                return;
            }
            //更新名称
            if (!permission.getName().equals(name.toString())) {
                permission.setName(name.toString());
                this.save(permission);
            }
        });

        if (!existsMap.isEmpty()) {
            List<Long> rmIds = existsMap.values().stream().map(Permission::getId).collect(Collectors.toList());
            this.repository.deleteAllByIdInBatch(rmIds);
        }
    }

    /**
     * 收集权限
     */
    private List<String> collectPermissions() {
        Map<String, RequestMappingHandlerMapping> map = SpringUtil.getBeansOfType(RequestMappingHandlerMapping.class);
        Pattern pattern = Pattern.compile("'(\\w+)'");
        Set<String> permissions = new TreeSet<>();
        map.forEach((name, bean) -> {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
            handlerMethods.forEach((info, handlerMethod) -> {
                PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), PreAuthorize.class);
                if (preAuthorize == null) {
                    preAuthorize = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), PreAuthorize.class);
                }
                if (preAuthorize == null) {
                    return;
                }

                Matcher matcher = pattern.matcher(preAuthorize.value());
                while (matcher.find()) {
                    permissions.add(matcher.group(1));
                }
            });
        });
        return new ArrayList<>(permissions);
    }

}
