package org.yml.jdbc.mapper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yml.jdbc.mapper.container.MapperContainer;
import org.yml.jdbc.mapper.entity.EntityInfo;
import org.yml.jdbc.mapper.sql.Sql;
import org.yml.jdbc.mapper.sql.Term;

import java.util.List;

/**
 * Created by yml on 2017/9/6.
 */
public class Query {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * 查询一个对象
     */
    public <T> T queryOne(Term<T> term) throws Exception {
        Sql<T> sql = term.getSql();
        List<T> tlist = jdbcTemplate.query(sql.getSql(), sql.getParams(), sql.getRowMapper());
        if (tlist == null || tlist.isEmpty()) {
            return null;
        }
        return tlist.get(0);
    }

    /**
     * 单表查询一个对象
     * 不支持连表查询
     */
    public <T> T querySingleById(Object id, Class<T> entityClass) throws Exception {
        String table = MapperContainer.getEntityTable(entityClass).getTableName();
        String sql = "select * from " + table + " where id=?";
        List<T> tlist = jdbcTemplate.query(sql, new BeanPropertyRowMapper(entityClass), id);
        if (tlist == null || tlist.isEmpty()) {
            return null;
        }
        return tlist.get(0);
    }

    /**
     * 查询一个对象
     */
    public <T> T queryById(Object id, Class<T> entityClass) throws Exception {
        Sql<T> sql = Term.select(entityClass).andEqualTo("id", id).getSql();
        List<T> tlist = jdbcTemplate.query(sql.getSql(), sql.getParams(), sql.getRowMapper());
        if (tlist == null || tlist.isEmpty()) {
            return null;
        }
        return tlist.get(0);
    }

    /**
     * 查询一个集合
     */
    public <T> List<T> queryList(Term<T> term) throws Exception {
        Sql<T> sql = term.getSql();
        return jdbcTemplate.query(sql.getSql(), sql.getParams(), sql.getRowMapper());
    }

    /**
     * count
     */
    public Integer count(Term term) throws Exception {
        Sql sql = term.countSql();
        return jdbcTemplate.queryForObject(sql.getSql(), sql.getParams(), Integer.class);
    }

    /**
     * 更新，根据主键
     */
    public <T> int modify(T t) throws Exception {
        Sql<T> sql = Term.modify(t).getSql();
        return jdbcTemplate.update(sql.getSql(), sql.getParams());
    }

    /**
     * 更新（修改，删除），根据Sql条件
     */
    public <T> int update(Term<T> term) throws Exception {
        Sql<T> sql = term.getSql();
        return jdbcTemplate.update(sql.getSql(), sql.getParams());
    }

    /**
     * 插入,返回自增主键
     */
    public <T> Number insert(T t) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new InsertPreparedStatementCreator(t), keyHolder);
        System.out.println("自动插入id============================" + keyHolder.getKey().toString());
        return keyHolder.getKey();
    }

    /**
     * 插入,返回表中数据
     */
    public <T> T insertObject(T t) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new InsertPreparedStatementCreator(t), keyHolder);
        Number id = keyHolder.getKey();
        System.out.println("自动插入id============================" + id.toString());
        return (T) queryById(id, t.getClass());
    }
    /**
     * 批量插入
     */
    public int[] insertBantch(List list,Class entityClass) throws Exception {
        long times = System.currentTimeMillis();
        EntityInfo entityInfo =MapperContainer.getEntityInfo(entityClass);
        BatchPreparedStatementSetter batchPreparedStatementSetter = new InsertBatchPreparedStatementSetter(list,entityInfo);
        times = System.currentTimeMillis() - times;
        return jdbcTemplate.batchUpdate(entityInfo.getInsertSqlAllColumns(),batchPreparedStatementSetter);
    }
    /**
     * 删除根据条件
     */
    public <T> int delete(T t) throws Exception {
        Sql<T> sql = Term.delete(t).getSql();
        return jdbcTemplate.update(sql.getSql(), sql.getParams());
    }

    /**
     * 删除根据ID
     */
    public <T> int deleteById(Object id, Class<T> entityClass) throws Exception {
        Sql<T> sql = Term.delete(entityClass).andEqualTo("id",id).getSql();
        return jdbcTemplate.update(sql.getSql(), sql.getParams());
    }
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
