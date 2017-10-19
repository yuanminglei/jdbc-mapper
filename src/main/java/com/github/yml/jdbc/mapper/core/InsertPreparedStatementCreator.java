package com.github.yml.jdbc.mapper.core;

import com.github.yml.jdbc.mapper.sql.SaveHolder;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yml on 2017/9/6.
 */
public class InsertPreparedStatementCreator<T> implements PreparedStatementCreator {
    protected T t;

    public InsertPreparedStatementCreator(T t) {
        this.t = t;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        try {
            SaveHolder holder = SaveHolder.build(t);
            PreparedStatement ps = connection
                    .prepareStatement(holder.getInsertSql(), holder.getColumnNames());
            for(int i=1;i<=holder.getColumnValue().length;i++){
                ps.setObject(i,holder.getColumnValue()[i-1]);
            }
            return ps;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }
}
