package org.yml.jdbc.mapper.entity;


import org.yml.jdbc.mapper.annotation.Join;
import org.yml.jdbc.mapper.annotation.JoinBean;
import org.yml.jdbc.mapper.annotation.JoinType;
import org.yml.jdbc.mapper.utils.StringUtils;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by yml on 2017/9/2.
 */
public class SqlBean<T> {
    protected Class<T> entityClass;
    protected List<EntityTable> entityTables = new ArrayList();
    protected Map<Class, EntityTable> entityTableMap = new HashMap();
    protected Map<String, EntityTable> aliasTableMap ;
    protected boolean isJoin =false;
    protected String selectSql;
    protected Map<Class, String> selectSqlMap = new HashMap();
    protected String fromSql;

    public SqlBean(Class<T> entityClass) {
        this.entityClass=entityClass;
        setEntityTables(entityClass);
    }
    public EntityTable getEntityTable(Class<?> entityClass) throws Exception {
            if(entityTables==null || entityTables.isEmpty()){
                throw new Exception("不存在的 EntityTable");
            }else if(entityTables.size()==1){
                return entityTables.get(0);
            }
       return entityTableMap.get(entityClass);
    }

    public void setEntityTables(Class<?> entityClass) {
        Annotation[] classAnnotation = entityClass.getAnnotations();
        for (Annotation cAnnotation : classAnnotation) {
            if (Table.class.equals(cAnnotation.annotationType())) {
                EntityTable entityTable= new EntityTable(entityClass,null,false);
                entityTables.add(entityTable);
                return;
            } else if (JoinBean.class.equals(cAnnotation.annotationType())) {
                try {
                    parseJoinBean(entityClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public void parseJoinBean(Class<?> beanClass) throws Exception {
        List<Field> list = Arrays.asList(beanClass.getDeclaredFields());
        for (Field field : list) {
            Class<?> entityClass = field.getType();
            String fieldName = field.getName();
            EntityTable entityTable;
            //连表查询的实体
            if (field.isAnnotationPresent(Join.class)) {
                Annotation annotation = field.getAnnotation(Join.class);
                if (annotation != null) {
                    Join join = (Join) annotation;
                    entityTable = new EntityTable(entityClass,fieldName, true,join);
                } else {
                    throw new Exception(entityClass + " Join 注解获取异常");
                }
            } else {
                //普通实体
                entityTable = new EntityTable(entityClass,fieldName,true);
            }

            entityTables.add(entityTable);
        }
        if(entityTables.size()>1) {
            isJoin = true;
            aliasTableMap = new HashMap();
        }else{
            throw new Exception("关联查询错误，缺少关联对象");
        }
        for (EntityTable entityTable : entityTables) {
            if (entityTable.getOnEntity() != null) {
                EntityTable OnEntityTable = entityTableMap.get(entityTable.getOnEntity());
                entityTable.setOnEntityTable(OnEntityTable);
            }
            entityTableMap.put(entityTable.getEntityClass(), entityTable);
            aliasTableMap.put(entityTable.getAlias(),entityTable);
        }
    }
    public EntityTable getEntityTableByAlias(String alias){
        if(aliasTableMap==null) return null;
        return aliasTableMap.get(alias);
    }
    /**
     * 多表查询时，指定查询结果只要来自哪个表的内容，不指定默认查询全部表的字段
     * */
    public String getSelect(Class<?> entityClass) {
        if(entityClass!=null && selectSqlMap.containsKey(entityClass)){
           return selectSqlMap.get(entityClass);
        }
        if(!StringUtils.isEmpty(selectSql)){
            return selectSql;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        if(isJoin){
            for (EntityTable element : entityTables) {
                String key = element.getAlias();
                sb.append("'");
                sb.append(key);
                sb.append("' as entityName ,");
                sb.append(key);
                sb.append(".* ,");
                selectSqlMap.put(element.getEntityClass(),"select "+key+".*");
            }
            String sql = sb.toString().trim();
            if (sql.endsWith(",")) {
                sql = sql.substring(0,sql.length() - 1);
            }
            if(entityClass!=null && selectSqlMap.containsKey(entityClass)){
                return selectSqlMap.get(entityClass);
            }
            return sql;
        }else{
            sb.append("*");
        }
        selectSql = sb.toString();
        return selectSql;
    }

    public String getFrom() {
        if(!StringUtils.isEmpty(fromSql)){
            return fromSql;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(" from ");
        if(isJoin) {
            for (EntityTable element : entityTables) {
                JoinType joinType = element.getJoinType();
                if (joinType != null) {
                    sb.append(" ");
                    sb.append(joinType.name);
                    sb.append(" ");
                }
                sb.append(element.getTableName());
                sb.append(" ");
                sb.append(element.getAlias());
                if (joinType != null) {
                    sb.append(" on ");
                    sb.append(element.getAlias());
                    sb.append(".");
                    sb.append(element.getByColumn());
                    sb.append("=");
                    sb.append(element.getOnEntityTable().getAlias());
                    sb.append(".");
                    sb.append(element.getOnColumn());
                }
            }
        }else{
            sb.append(entityTables.get(0).getTableName());
        }
        fromSql = sb.toString();
        return fromSql;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public Map<String, EntityTable> getAliasTableMap() {
        return aliasTableMap;
    }
}
