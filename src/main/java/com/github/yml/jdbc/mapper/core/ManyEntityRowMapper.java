package com.github.yml.jdbc.mapper.core;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import com.github.yml.jdbc.mapper.entity.EntityColumn;
import com.github.yml.jdbc.mapper.entity.EntityTable;
import com.github.yml.jdbc.mapper.sql.Sql;
import com.github.yml.jdbc.mapper.utils.MetaObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 用于关联查询
 * Created by yml on 2017/9/5.
 */
public class ManyEntityRowMapper<T> implements RowMapper<T> {
    protected Sql<T> joinSelectSql;

    public ManyEntityRowMapper(Sql<T> joinSelectSql) {
        this.joinSelectSql = joinSelectSql;
    }

    @Override
    public T mapRow(ResultSet rs, int i) throws SQLException {
        MetaObject<T> mappedObject = MetaObject.forClass(joinSelectSql.getBeanClass());
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            EntityTable currEntityTable = null;
            MetaObject currEntity = null;
            for (int index = 1; index <= columnCount; ++index) {
                String column = JdbcUtils.lookupColumnName(rsmd, index);
                if ("entityName".equals(column)) {
                    if(currEntity!=null){
                        mappedObject.setValue(currEntityTable.getFkName(),currEntity.getBean());
                    }
                    String alias = (String) JdbcUtils.getResultSetValue(rs, index, String.class);
                    currEntityTable = joinSelectSql.getEntityTableByAlias(alias);
                    currEntity =MetaObject.forClass(currEntityTable.getEntityClass());
                    continue;
                }
                EntityColumn property = currEntityTable.getEntityColumn(column);
                Object value = JdbcUtils.getResultSetValue(rs, index, property.getPropertyClass());
                currEntity.setValue(property.getProperty(),value);
            }
            if(currEntity!=null){
                mappedObject.setValue(currEntityTable.getFkName(),currEntity.getBean());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return mappedObject.getBean();
    }
}
