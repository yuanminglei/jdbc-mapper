package com.github.yml.jdbc.mapper.sql;


import com.github.yml.jdbc.mapper.container.MapperContainer;
import com.github.yml.jdbc.mapper.entity.SqlBean;
import com.github.yml.jdbc.mapper.entity.EntityTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yml on 2017/9/2.
 */
public class Condition<T> {

    protected String orderByClause;
    protected String groupByClause;
    protected String limit;
    protected boolean distinct =false;
    protected boolean exists;
    protected boolean notNull;
    protected Class<T> beanClass;
    protected SqlBean sqlBean;
    protected List<CriterionGroup> criterionGroups;
    protected List<Object> paramValues= new ArrayList<>();
    private String whereSql;


    /**
     * 默认exists为true
     */
    public Condition(Class<T> beanClass) {
        this(beanClass, true,false);
    }


    /**
     * 带exists参数的构造方法
     *
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     * @param notNull     - true时，如果值为空，就会抛出异常，false时，如果为空就不使用该字段的条件
     */
    public Condition(Class<T> beanClass, boolean exists, boolean notNull) {
        this.exists = exists;
        this.notNull = notNull;
        criterionGroups = new ArrayList<CriterionGroup>();
        this.beanClass=beanClass;
        sqlBean= MapperContainer.getSqlBean(beanClass);
    }
    public String  getModify(SaveHolder<T> holder) throws Exception {
        String sql = holder.getUpdateSql();
        for(Object value : holder.getColumnValue()){
            paramValues.add(value);
        }
        return sql;
    }
    public String getOrderBy() {
        return orderByClause;
    }
    public String getGroupBy() {
        return groupByClause;
    }
    public String getLimit() {
        return limit;
    }
    public String getSelect(Class<?> entityClass) {
        return sqlBean.getSelect(entityClass);
    }
    public String getFrom() {
        return sqlBean.getFrom();
    }
    public String getWhere() {
        if(whereSql==null || whereSql.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append(" where 1=1");
            if (criterionGroups != null) {
                for (CriterionGroup group : criterionGroups) {
                    if (!group.isEmpty()) {
                        sb.append(" ").append(group.getAndOr());
                        if (criterionGroups.size() > 1) {
                            sb.append(" (");
                        }
                        sb.append(" ").append(group.getSql());
                        if (criterionGroups.size() > 1) {
                            sb.append(") ");
                        }
                        paramValues.addAll(group.getParamValues());
                    }
                }
            }
            return whereSql=sb.toString();
        }
        return whereSql;
    }

    public void limit(Integer num1,Integer num2) throws SQLException {
        if(num1==null || num2==null){
            throw new SQLException("num1 and num2 must not null !");
        }
        limit = new String();
        limit = "limit "+num1+","+num2;
    }
    public void limit(Integer num) throws SQLException{
        if(num==null || num<=0){
            throw new SQLException("num !=null and num>0");
        }
        limit = new String();
        limit = "limit "+num;
    }
    public void orderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }
    public List<Object> getParamValues() {
        return paramValues;
    }
    public Object[] getParams() {
        return paramValues.toArray();
    }
    public Class<T> getBeanClass() {
        return beanClass;
    }
    public SqlBean getSqlBean() {
        return sqlBean;
    }
    public Map<String, EntityTable> getAliasTableMap(){
        return sqlBean.getAliasTableMap();
    }


    public CriterionGroup and() {
        CriterionGroup group = new CriterionGroup("and",sqlBean,exists,notNull);
        criterionGroups.add(group);
        return group;
    }
    public  CriterionGroup or() {
        CriterionGroup group = new CriterionGroup("or",sqlBean,exists,notNull);
        criterionGroups.add(group);
        return group;
    }

    public void clear() {
        criterionGroups.clear();
        orderByClause = null;
        distinct = false;
    }
    public Class<T> getEntityClass() {
        return sqlBean.getEntityClass();
    }


    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public void groupByColumn(String column) {
        this.groupByClause = column;
    }
}
