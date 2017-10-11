package org.yml.jdbc.mapper;


import org.yml.jdbc.mapper.core.Query;
import org.yml.jdbc.mapper.sql.Term;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yml on 2017/9/5.
 */
public class Mapper extends Query {

    public <T> List<T> listWithLastId(T t, Number lastId, int ps) {
        try {
            Term example = Term.select(t);
            if (lastId != null && lastId.longValue() != 0) {
                example.andLessThan("id", lastId);
            }
            example.orderByClause("id desc");
            example.limit(ps);
            return super.queryList(example);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询一个对象
     */
    public <T> T findOne(T t) {
        try {
            return super.queryOne(Term.select(t).limit(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public <T>List<T> list(Class<T> entityClass){
        try {
            return super.queryList(Term.select(entityClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public <T>List<T> list(Class<T> entityClass, String orderStr){
        try {
            return super.queryList(Term.select(entityClass).orderByClause(orderStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 默认排序，id逆序
     * */
    public <T>List<T> list(T t){
        try {
            return super.queryList(Term.select(t));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public <T>List<T> list(T t, String orderStr){
        try {
            return super.queryList(Term.select(t).orderByClause(orderStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public <T>T selectOne(T t) throws Exception {
        return queryOne(Term.select(t));
    }

    public  <T>Integer count(T t) throws Exception{
        return super.count(Term.select(t));
    }
    /**
     * @param orderStr: 默认值“id desc”
     * */
    public <T>PageHelper<T> list(T t, String orderStr, Integer pageNum, Integer pageSize) throws Exception{
        if(orderStr == null){
            orderStr = "id desc";
        }
        List<T> list = new ArrayList<T>();
        Integer toalSize = count(t);
        if(toalSize>0){
            int offset = (pageNum-1)*pageSize;
            list = queryList(Term.select(t).orderByClause(orderStr).limit(offset,pageSize));
        }
        return new PageHelper(toalSize,pageSize,pageNum,list);
    }
    /**
     * */
    public <T>PageHelper<T> list(Term<T> term, Integer pageNum, Integer pageSize) throws Exception{
        List<T> list = new ArrayList<T>();
        Integer toalSize = count(term);
        if(toalSize>0){
            int offset = (pageNum-1)*pageSize;
            list = queryList(term.limit(offset,pageSize));
        }
        return new PageHelper(toalSize,pageSize,pageNum,list);
    }
}
