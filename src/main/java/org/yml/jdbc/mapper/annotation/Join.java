package org.yml.jdbc.mapper.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Join {
    //别名
    String alias() default "";
    //on 连接条件实体
    Class<?> onEntity();
    //连接的字段
    String onColumn();
    //被连接字段
    String byColumn();
    //连接类型
    JoinType joinType() default JoinType.LEFT_OUTER_JOIN;

}
