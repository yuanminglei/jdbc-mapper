package org.yml.jdbc.mapper.sql;

import org.yml.jdbc.mapper.entity.EntityTable;
import org.yml.jdbc.mapper.entity.SqlBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yml on 2017/9/5.
 */
public class CriterionGroup {
    //连接条件
    protected String andOr;
    protected List<Criterion> criterions;
    protected SqlBean sqlBean;
    //字段是否必须存在
    protected boolean exists;
    //值是否不能为空
    protected boolean notNull;

    protected String sql;
    protected List<Object>paramValues;

    public CriterionGroup(String andOr, SqlBean sqlBean, boolean exists, boolean notNull){
        this.andOr=andOr;
        this.sqlBean=sqlBean;
        this.exists=exists;
        this.notNull=notNull;
        criterions = new ArrayList<Criterion>();
    }
    /**
     * 用于关联查询
     * */
    public Criteria criteria(Class<?> entityClass) throws Exception {
         return new Criteria(sqlBean.getEntityTable(entityClass),this);
    }

    public boolean isEmpty(){
        if(criterions==null || criterions.isEmpty()){
            return true;
        }
        return false;
    }

    public void buildSql(){
        paramValues = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        int i=1;
        for(Criterion criterion:criterions){
            if(i>1) {
                sb.append(criterion.getAndOr());
                sb.append(" ");
            }
            sb.append(criterion.getCondition());
            if(criterion.isNoValue()){
            }else if(criterion.isListValue()){
                 if(criterion.getValue() instanceof Collection<?>){
                     Collection values = (Collection)criterion.getValue();
                     if(!values.isEmpty()){
                         sb.append("(");
                         for(Object v:values){
                             paramValues.add(v);
                             sb.append("?,");
                         }
                         sb.deleteCharAt(sb.length()-1);
                         sb.append(")");
                     }
                 }
            }else if(criterion.isBetweenValue()){
                sb.append(" ? and ? ");
                paramValues.add(criterion.getValue());
                paramValues.add(criterion.getSecondValue());
            }else{
                sb.append(" ? ");
                paramValues.add(criterion.getValue());
            }
            i++;
        }
        sql = sb.toString();
    }

    public String getSql() {
        if(sql==null || sql.isEmpty()){
            buildSql();
        }
        return sql;
    }

    public List<Object> getParamValues() {
        return paramValues;
    }

    public SqlBean getSqlBean() {
        return sqlBean;
    }

    public void setSqlBean(SqlBean sqlBean) {
        this.sqlBean = sqlBean;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public void setCriterions(List<Criterion> criterions) {
        this.criterions = criterions;
    }
}
