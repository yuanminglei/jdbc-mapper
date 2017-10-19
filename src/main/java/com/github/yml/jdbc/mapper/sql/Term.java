package com.github.yml.jdbc.mapper.sql;

/**
 * Created by yml on 2017/9/8.
 */

import com.github.yml.jdbc.mapper.utils.StringUtils;

import java.sql.SQLException;

public class Term<T>{
    private Condition condition;
    private CriterionGroup criterionGroup;
    private Criteria criteria;
    private String sql;
    //内部常量，用于限制，删除更新等操作必须指定where条件
    private boolean isNotNullWhere = false;
    //多表查询，指定具体只返回哪个表的结果，不指定则全查询
    private Class<T> rsEntityClass;
    private Term(Class <T>beanClass) {
        this.condition=new Condition(beanClass);
        and();
    }
    private Term(Class<T> rsEntityClass,Class beanClass) {
        this.condition=new Condition(beanClass);
        this.rsEntityClass = rsEntityClass;
        and();

    }
    /**
     * @param rsClass 多表查询，指定具体只返回哪个表的结果，不指定则全查询
     * @param beanClass 多表查询关联关系封装类，例见 demo 下 Test类
     * */
    public static <T>Term<T> select(Class<T> rsClass,Class beanClass){
        Term example =new Term(rsClass,beanClass);
        example.selectSql();
        return example;
    }
    public static <T>Term<T> select(Class<T> beanClass){
        Term example =new Term(beanClass);
        example.selectSql();
        return example;
    }
    public static <T>Term<T> select(T t) throws Exception {
        Term example =new Term(t.getClass());
        example.andEqualTo(t);
        example.selectSql();
        return example;
    }
    public static <T> Term<T> modify(T t) throws Exception {
        Term example =new Term(t.getClass());
        example.modifySql(t);
        example.isNotNullWhere = true;
        return example;
    }
    public static <T> Term<T> delete(Class<T> beanClass){
        Term example =new Term(beanClass);
        example.deleteSql();
        example.isNotNullWhere = true;
        return example;
    }
    public static <T> Term<T> delete(T t)throws Exception {
        Term example =new Term(t.getClass());
        example.andEqualTo(t);
        example.deleteSql();
        example.isNotNullWhere = true;
        return example;
    }


    private void selectSql(){  sql = condition.getSelect(rsEntityClass) + " " + condition.getFrom() ;}
    private void deleteSql(){
        sql = "delete " + condition.getFrom();
    }
    private void modifySql(T t) throws Exception {
        if(t==null) {
            throw new Exception("update 必须指定更新内容");
        }
        SaveHolder holder = SaveHolder.build(t);
        String id = holder.getIdProperty();
        Object value = holder.getValue(id);
        if(value!=null){
            andEqualTo(id,value);
        }
        sql = condition.getModify(holder);
    }


    public Sql<T> getSql() throws Exception {
        if(isNotNullWhere && criteria==null){
            throw new Exception("条件不能为空");
        }
        sql = sql+ " " + condition.getWhere();

        if(!StringUtils.isEmpty(condition.getGroupBy())){
            sql = sql+" group by "+condition.getGroupBy();
        }
        if(!StringUtils.isEmpty(condition.getOrderBy())){
            sql = sql+" order by "+condition.getOrderBy();
        }
        if(!StringUtils.isEmpty(condition.getLimit())){
            sql = sql+" "+condition.getLimit();
        }
        if(rsEntityClass!=null){
            return new Sql(sql,condition.getParams(),rsEntityClass,null);
        }
        return new Sql(sql,condition.getParams(),condition.getBeanClass(),condition.getAliasTableMap());
    }
    public Sql<T> countSql()throws Exception{
        if(isNotNullWhere && criteria==null){
            throw new Exception("条件不能为空");
        }
        String countSql = "select count(0) " + condition.getFrom()+ " " + condition.getWhere();
        return new Sql(countSql,condition.getParams(),condition.getBeanClass(),condition.getAliasTableMap());
    }
    /**全局方法开始----------------------------------------------------**/
    public boolean isDistinct() {
        return condition.isDistinct();
    }
    public Term<T> setDistinct(boolean distinct) {
        condition.setDistinct(distinct);
        return this;
    }

    /**
     * @param filed 需要group by 的类属性
     * */
    public Term<T> groupBy(String filed) throws Exception {
        condition.groupByColumn(criteria().column(filed));
        return this;
    }
    /**
     * @param column 需要group by 的列名
     * */
    public Term<T> groupByColumn(String column) throws Exception {
        condition.groupByColumn(column);
        return this;
    }
    /**
     * @param orderByFiled 需要orderBy 类属性
     * */
    public Term<T> orderBy(String orderByFiled) throws Exception {
        String orderByClause = criteria().column(orderByFiled);
        condition.orderByClause(orderByClause);
        return this;
    }
    /**
     * @param orderByFiled orderByDesc 类属性
     * */
    public Term<T> orderByDesc(String orderByFiled) throws Exception {
        String orderByClause = criteria().column(orderByFiled)+" desc";
        condition.orderByClause(orderByClause);
        return this;
    }
    /**
     * @param orderByClause order by 后面的内容，必须是列名。
     * */
    public Term<T> orderByClause(String orderByClause){
        condition.orderByClause(orderByClause);
        return this;
    }
    public Term<T> limit(Integer num1, Integer num2) throws SQLException {
        condition.limit(num1, num2);
        return this;
    }
    public Term<T> limit(Integer num) throws SQLException{
        condition.limit(num);
        return this;
    }
    //做大分组时使用如 (a=1 or c=2) and  (d>3 or f<4) 中的and,后续添加条件如d,f
    public Term<T> and() {
        criterionGroup = condition.and();
        criteria = null;
        return this;
    }
    //做大分组时使用如 (a=1 and c=2) or  (d>3 and f<4)中的or,后续添加条件如d,f
    public Term<T> or() {
        criterionGroup = condition.or();
        criteria = null;
        return this;
    }
    /***
     * 多表查询时，指定条件来自哪张表,非多表查询无需指定
     */
    public Term<T> table(Class<?> entityClass) throws Exception {
        criteria = criterionGroup.criteria(entityClass);
        return this;
    }
    /***
     * 多表查询时，指定条件来自哪张表,非多表查询无需指定
     */
    public Term<T> table(Object a) throws Exception {
        criteria = criterionGroup.criteria(a.getClass());
        andEqualTo(a);
        return this;
    }
    private Criteria criteria() throws Exception{
        if(criteria==null){
            criteria = criterionGroup.criteria(null);
        }
        return criteria;
    }

    /**添加条件方法开始**/
    public Term<T> andEqualTo(Object a) throws Exception {
        criteria().andEqualTo(a);
        return  this;
    }

    public Term<T> orEqualTo(T t) throws Exception {
        criteria().orEqualTo(t);
        return  this;
    }

    public Term<T> andIsNull(String property) throws Exception {
        criteria().andIsNull(property);
        return  this;
    }

    public Term<T> andIsNotNull(String property) throws Exception {
        criteria().andIsNotNull(property);
        return  this;
    }

    public Term<T> andEqualTo(String property, Object value) throws Exception {
        criteria().andEqualTo(property,value);
        return  this;
    }

    public Term<T> andNotEqualTo(String property, Object value) throws Exception {
        criteria().andNotEqualTo(property,value);
        return  this;
    }

    public Term<T> andGreaterThan(String property, Object value) throws Exception {
        criteria().andGreaterThan(property,value);
        return  this;
    }

    public Term<T> andGreaterThanOrEqualTo(String property, Object value) throws Exception {
        criteria().andGreaterThanOrEqualTo(property,value);
        return  this;
    }

    public Term<T> andLessThan(String property, Object value) throws Exception {
        criteria().andLessThan(property,value);
        return  this;
    }

    public Term<T> andLessThanOrEqualTo(String property, Object value) throws Exception {
        criteria().andLessThanOrEqualTo(property,value);
        return  this;
    }

    public Term<T> andIn(String property, Iterable values) throws Exception {
        criteria().andIn(property,values);
        return  this;
    }

    public Term<T> andNotIn(String property, Iterable values) throws Exception {
        criteria().andNotIn(property,values);
        return  this;
    }

    public Term<T> andBetween(String property, Object value1, Object value2) throws Exception {
        criteria().andBetween(property,value1,value2);
        return  this;
    }

    public Term<T> andNotBetween(String property, Object value1, Object value2) throws Exception {
        criteria().andNotBetween(property,value1,value2);
        return  this;
    }

    public Term<T> andLike(String property, String value) throws Exception {
        criteria().andLike(property,value);
        return  this;
    }

    public Term<T> andNotLike(String property, String value) throws Exception {
        criteria().andNotLike(property,value);
        return  this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Term<T> andCondition(String condition) throws Exception {
        criteria().andCondition(condition);
        return  this;
    }

    /**
     * 手写左边条件，右边用value值
     *
     * @param condition 例如 "length(countryname)="
     * @param value     例如 5
     * @return
     */
    public Term<T> andCondition(String condition, Object value)throws Exception {
        criteria().andCondition(condition,value);
        return  this;
    }

    public Term<T> orIsNull(String property) throws Exception {
        criteria().orIsNull(property);
        return  this;
    }

    public Term<T> orIsNotNull(String property) throws Exception {
        criteria().orIsNotNull(property);
        return  this;
    }

    public Term<T> orEqualTo(String property, Object value) throws Exception {
        criteria().orEqualTo(property,value);
        return  this;
    }

    public Term<T> orNotEqualTo(String property, Object value) throws Exception {
        criteria().orNotEqualTo(property,value);
        return  this;
    }

    public Term<T> orGreaterThan(String property, Object value) throws Exception {
        criteria().orGreaterThan(property,value);
        return  this;
    }

    public Term<T> orGreaterThanOrEqualTo(String property, Object value) throws Exception {
        criteria().orGreaterThanOrEqualTo(property,value);
        return  this;
    }

    public Term<T> orLessThan(String property, Object value) throws Exception {
        criteria().orLessThan(property,value);
        return  this;
    }

    public Term<T> orLessThanOrEqualTo(String property, Object value) throws Exception {
        criteria().orLessThanOrEqualTo(property,value);
        return  this;
    }

    public Term<T> orIn(String property, Iterable values) throws Exception {
        criteria().orIn(property,values);
        return  this;
    }

    public Term<T> orNotIn(String property, Iterable values) throws Exception {
        criteria().orNotIn(property,values);
        return  this;
    }

    public Term<T> orBetween(String property, Object value1, Object value2) throws Exception {
        criteria().orBetween(property,value1,value2);
        return  this;
    }

    public Term<T> orNotBetween(String property, Object value1, Object value2) throws Exception {
        criteria().orNotBetween(property,value1,value2);
        return  this;
    }

    public Term<T> orLike(String property, String value) throws Exception {
        criteria().orLike(property,value);
        return  this;
    }

    public Term<T> orNotLike(String property, String value) throws Exception {
        criteria().orNotLike(property,value);
        return  this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Term<T> orCondition(String condition) throws Exception {
        criteria().orCondition(condition);
        return  this;
    }

    /**
     * 手写左边条件，右边用value值
     *
     * @param condition 例如 "length(countryname)="
     * @param value     例如 5
     * @return
     */
    public Term<T> orCondition(String condition, Object value) throws Exception{
        criteria().orCondition(condition,value);
        return  this;
    }

}