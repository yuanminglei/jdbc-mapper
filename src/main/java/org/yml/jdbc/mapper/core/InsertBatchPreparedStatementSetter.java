package org.yml.jdbc.mapper.core;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.yml.jdbc.mapper.entity.EntityColumn;
import org.yml.jdbc.mapper.entity.EntityInfo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by yml on 2017/9/12.
 */
public class InsertBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private List list;
    private EntityInfo entityInfo;
    long times = System.currentTimeMillis();
    private static ConversionService conversionService = new DefaultConversionService();
    public InsertBatchPreparedStatementSetter(List list,EntityInfo entityInfo){
        this.list=list;
        this.entityInfo =entityInfo;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Object t = list.get(i);
        BeanWrapperImpl bw =(BeanWrapperImpl) PropertyAccessorFactory.forBeanPropertyAccess(t);
        bw.setConversionService(conversionService);
        int index=1;
        for (EntityColumn entityColumn: entityInfo.getEntityColumns()) {
            String column = entityColumn.getColumn();
            if(!column.equals(entityInfo.getIdProperty())
                    && !column.equals("create_time") && !column.equals("update_time") ){
                Object value = bw.getPropertyValue(entityColumn.getProperty());
                ps.setObject(index,value);
                index++;
            }
        }
      //  Long a =System.currentTimeMillis() - times;
     //   System.out.println(ps.getConnection()+" -"+i+"- "+ ps.getConnection().getAutoCommit() +",==="+a);
    }

    @Override
    public int getBatchSize() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }
}
