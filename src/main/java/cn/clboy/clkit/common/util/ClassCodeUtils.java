package cn.clboy.clkit.common.util;

import cn.hutool.core.util.StrUtil;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import org.springframework.util.CollectionUtils;

import java.util.StringJoiner;

/**
 * 类代码实用程序
 *
 * @author clboy
 * @date 2024/05/14 11:34:32
 */
public class ClassCodeUtils {


    /**
     * 包装类代码，继承或实现目标类，重写方法调用持有目标对象方法
     *
     * @param clazz class
     */
    public static String genWrapperClassCode(Class<?> clazz) {
        //TODO
        throw new UnsupportedOperationException();
    }

    /**
     * 包装类代码，继承或实现目标类，重写方法调用持有目标对象方法
     *
     * @param sourceCode 源代码
     */
    public static String genWrapperClassCode(String sourceCode) {
        CompilationUnit targetUnit = StaticJavaParser.parse(sourceCode);
        TypeDeclaration<?> targetType = targetUnit.getType(0);
        if (!targetType.isClassOrInterfaceDeclaration()) {
            throw new IllegalArgumentException("目标非Class或Interface");
        }
        ClassOrInterfaceDeclaration targetClass = targetType.asClassOrInterfaceDeclaration();
        NodeList<TypeParameter> classTypeParameters = targetClass.getTypeParameters();
        String targetClassSimpleName = targetClass.getNameAsString();
        String targetClassName = targetClass.getFullyQualifiedName().orElse(targetClassSimpleName);
        String targetClassCamelCaseName = StrUtil.lowerFirst(targetClassSimpleName);

        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setImports(targetUnit.getImports());
        compilationUnit.addImport(targetClassName);
        ClassOrInterfaceDeclaration wrapperClass = compilationUnit.addClass(targetClassSimpleName + "Wrapper");
        wrapperClass.setPublic(true);
        wrapperClass.setTypeParameters(classTypeParameters);
        ClassOrInterfaceType superType = new ClassOrInterfaceType();
        superType.setName(targetClassSimpleName);
        if (!CollectionUtils.isEmpty(classTypeParameters)) {
            superType.setTypeArguments(classTypeParameters.stream().map(tp -> (Type) tp).toArray(Type[]::new));
        }
        if (targetClass.isInterface()) {
            wrapperClass.addImplementedType(superType);
        } else {
            wrapperClass.addExtendedType(superType);
        }

        //目标变量
        FieldDeclaration targetField = wrapperClass.addPrivateField(targetClassSimpleName, targetClassCamelCaseName);
        targetField.setFinal(true);

        ConstructorDeclaration constructor = wrapperClass.addConstructor();
        constructor.addParameter(targetClassSimpleName, targetClassCamelCaseName);
        constructor.setPublic(true);
        BlockStmt constructorBody = new BlockStmt();
        constructorBody.addStatement(String.format("this.%s = %s;", targetClassCamelCaseName, targetClassCamelCaseName));
        constructor.setBody(constructorBody);

        //实现方法，调用目标
        for (MethodDeclaration method : targetClass.getMethods()) {
            if (!targetClass.isInterface() && !method.isPublic()) {
                continue;
            }
            if (method.isStatic() || method.isNative()) {
                continue;
            }
            NodeList<TypeParameter> typeParameters = method.getTypeParameters();
            MethodDeclaration proxyMethod = wrapperClass.addMethod(method.getNameAsString());
            proxyMethod.addAnnotation(new MarkerAnnotationExpr(Override.class.getSimpleName()));
            proxyMethod.setPublic(true);
            proxyMethod.setTypeParameters(typeParameters);
            proxyMethod.setType(method.getType());
            proxyMethod.setParameters(method.getParameters());
            BlockStmt proxyMethodBody = new BlockStmt();
            StringJoiner bodyText = new StringJoiner(",",
                    String.format("return this.%s.%s(", targetClassCamelCaseName, method.getNameAsString()),
                    ");");
            for (Parameter parameter : method.getParameters()) {
                bodyText.add(parameter.getNameAsString());
            }
            proxyMethodBody.addStatement(bodyText.toString());
            proxyMethod.setBody(proxyMethodBody);
        }

        return compilationUnit.toString();
    }
}
