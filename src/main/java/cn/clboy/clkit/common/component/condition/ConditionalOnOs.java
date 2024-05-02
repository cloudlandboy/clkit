package cn.clboy.clkit.common.component.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 条件是操作系统
 *
 * @author clboy
 * @date 2024/04/22 14:59:03
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnOsCondition.class)
public @interface ConditionalOnOs {

    String[] names();

}
