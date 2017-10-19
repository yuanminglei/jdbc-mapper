package com.github.yml.jdbc.mapper.container;

import com.github.yml.jdbc.mapper.entity.EntityInfo;
import com.github.yml.jdbc.mapper.entity.EntityTable;
import com.github.yml.jdbc.mapper.entity.SqlBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yml on 2017/9/7.
 */
public class MapperContainer {
    private static Map<Class, EntityInfo> entityMap = new HashMap<>();
    private static Map<Class, SqlBean> sqlBeanMap = new HashMap();

    public static EntityInfo getEntityInfo(Class c) {
        if (entityMap.containsKey(c)) {
            return entityMap.get(c);
        }
        EntityInfo entityInfo = new EntityInfo(c);
        entityMap.put(c, entityInfo);
        return entityInfo;
    }

    public static SqlBean getSqlBean(Class c) {
        if (sqlBeanMap.containsKey(c)) {
            return sqlBeanMap.get(c);
        }
        SqlBean sqlBean = new SqlBean(c);
        sqlBeanMap.put(c, sqlBean);
        return sqlBean;
    }

    public static<T> EntityTable<T> getEntityTable(Class<T> entityClass) throws Exception {
        SqlBean<T> sqlBean= getSqlBean(entityClass);
        if(sqlBean==null) return null;
        return sqlBean.getEntityTable(entityClass);
    }
}
