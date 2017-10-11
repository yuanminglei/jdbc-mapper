package org.yml.jdbc.mapper.sql;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.yml.jdbc.mapper.core.ManyEntityRowMapper;
import org.yml.jdbc.mapper.entity.EntityTable;

import java.util.Map;

/**
 * Created by yml on 2017/9/6.
 */
public class Sql<T> {
    protected String sql;
    protected Object[] params;
    protected Class<T> beanClass;
    protected Map<String,EntityTable> aliasMap;

    public Sql(String sql, Object[] params, Class<T> beanClass, Map<String, EntityTable> aliasMap) {
        this.sql = sql;
        this.params = params;
        this.beanClass = beanClass;
        this.aliasMap = aliasMap;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }
    public String getSql() {
        System.out.println("sql ："+sql);
        return sql;
    }

    public Object[] getParams() {
        if(params!=null) {
            System.out.print("sql 参数：");
            for (Object p : params) {
                System.out.print(p + " , ");
            }
            System.out.println("");
        }
        return params;
    }

    public EntityTable getEntityTableByAlias(String alias){
        return aliasMap.get(alias);
    }
    public RowMapper<T> getRowMapper(){
        if(aliasMap==null||aliasMap.isEmpty()){
            return new BeanPropertyRowMapper(beanClass);
        }
        return new ManyEntityRowMapper(this);
    }
}
