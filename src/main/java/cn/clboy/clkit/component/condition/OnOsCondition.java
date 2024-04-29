package cn.clboy.clkit.component.condition;

import cn.clboy.clkit.os.handler.OsHandler;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 在操作系统条件下
 *
 * @author clboy
 * @date 2024/04/22 15:01:01
 */
public class OnOsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnOs.class.getName());
        if (attributes == null) {
            return false;
        }
        String[] names = (String[]) attributes.get("names");

        for (String name : names) {
            if (OsHandler.OS_INFO.getName().startsWith(name)) {
                return true;
            }
        }
        return false;
    }
}
