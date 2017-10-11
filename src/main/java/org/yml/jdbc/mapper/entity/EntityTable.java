package org.yml.jdbc.mapper.entity;


import org.yml.jdbc.mapper.annotation.Join;
import org.yml.jdbc.mapper.annotation.JoinType;
import org.yml.jdbc.mapper.container.MapperContainer;
import org.yml.jdbc.mapper.utils.StringUtils;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by yml on 2017/9/2.
 */
public class EntityTable<T> {

    //用于关联查询，外层实体bean中的fieldName
    protected String fkName;
    //别名
    protected String alias;
    protected JoinType joinType;
    //连接的表
    protected EntityTable onEntityTable;
    //连接的表
    protected Class<?> onEntity;
    //连接的字段
    protected String onColumn;
    //被连接字段
    protected String byColumn;
    //实体信息
    protected EntityInfo entityInfo;
    /**
     * @param fkName 用于关联查询，外层实体bean中的fieldName
     * @param isJoin 是否是关联查询
     * */
    public EntityTable(Class<T> entityClass,String fkName,boolean isJoin) {
        entityInfo =  MapperContainer.getEntityInfo(entityClass);
        init(entityClass,fkName,isJoin,null);
    }
    /**
     * @param isJoin 是否是关联查询
     * */
    public EntityTable(Class<T> entityClass,String fkName,boolean isJoin, Join join) {
        entityInfo =  MapperContainer.getEntityInfo(entityClass);
        init(entityClass,fkName,isJoin,join);
    }
    protected void init(Class<T> entityClass,String fkName,boolean isJoin, Join join){
        if(join!=null) {
            EntityInfo onEntityInfo=  MapperContainer.getEntityInfo(join.onEntity());
            this.alias = join.alias();
            this.joinType = join.joinType();
            this.onEntity = join.onEntity();
            this.onColumn =join.onColumn();
            if(!onEntityInfo.containsColumn(join.onColumn())){
                this.onColumn = onEntityInfo.getEntityColumnByProperty(join.onColumn()).getColumn();
            }
            this.byColumn = join.byColumn();
            if(!entityInfo.containsColumn(join.byColumn())){
                this.byColumn =entityInfo.getEntityColumnByProperty(join.byColumn()).getColumn();
            }
        }
        this.fkName=fkName;
        setAlias(isJoin);
    }
    protected void setAlias(boolean isJoin){
        if(isJoin) {
            if (alias == null || alias.isEmpty()) {
                String[] tableNameStrs = entityInfo.getTableName().split("\\.");
                alias = tableNameStrs[tableNameStrs.length - 1];
            }
        }
    }
    public EntityColumn getEntityColumn(String column){
        return entityInfo.getEntityColumnByColumn(column);
    }
    public String getIdProperty() {
        return entityInfo.getIdProperty();
    }
    public String getProperty(String column){
        return entityInfo.getEntityColumnByColumn(column).getProperty();
    }
    public String getColumn(String property){
        EntityColumn entityColumn =entityInfo.getEntityColumnByProperty(property);
        String column =entityColumn.getColumn();
        if(!StringUtils.isEmpty(alias)){
            column = alias+"."+column;
        }
        return column;
    }


    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public String getTableName() {
        return entityInfo.getTableName();
    }

    public Class<?> getEntityClass() {
        return entityInfo.getEntityClass();
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public Class<?> getOnEntity() {
        return onEntity;
    }

    public void setOnEntity(Class<?> onEntity) {
        this.onEntity = onEntity;
    }

    public EntityTable getOnEntityTable() {
        return onEntityTable;
    }

    public void setOnEntityTable(EntityTable onEntityTable) {
        this.onEntityTable = onEntityTable;
    }

    public String getOnColumn() {
        return onColumn;
    }

    public void setOnColumn(String onColumn) {
        this.onColumn = onColumn;
    }

    public String getByColumn() {
        return byColumn;
    }

    public void setByColumn(String byColumn) {
        this.byColumn = byColumn;
    }

    public String getFkName() {
        return fkName;
    }

    public boolean containsProperty(String property) {
       return entityInfo.containsProperty(property);
    }


    public List<String> getProperties() {
        return entityInfo.getProperties();
    }

    public Class<?> getPropertyClass(String property) {
        return entityInfo.getEntityColumnByProperty(property).getPropertyClass();
    }
}
