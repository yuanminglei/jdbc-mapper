package com.github.yml.jdbc.mapper.sql;


import com.github.yml.jdbc.mapper.container.MapperContainer;
import com.github.yml.jdbc.mapper.entity.EntityTable;
import com.github.yml.jdbc.mapper.utils.MetaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yml on 2017/9/6.
 */
public class SaveHolder<T> {

    private String columnNames[];
    private Object columnValue[];
    private Class<?> columnType[];
    private MetaObject<T> metaObject;
    private EntityTable<T> entityTable;
    private  String idProperty;
    private  String idColumn;
    private  Object idValue;
    private  Class idType;
    public static <T> SaveHolder build(T t) throws Exception {
        SaveHolder holder = new SaveHolder();
        holder.entityTable = (EntityTable<T>) MapperContainer.getEntityTable(t.getClass());
        if (holder.entityTable == null) return null;
        holder.init(t);
        return holder;
    }

    private void init(T t) {
        List<String> columnList = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        List<Class> typeList = new ArrayList<>();
        metaObject = MetaObject.forObject(t);
        idProperty = entityTable.getIdProperty();
        for (String property : entityTable.getProperties()) {
            Object value = metaObject.getValue(property);
            Class<?> type = entityTable.getPropertyClass(property);
            String column = entityTable.getColumn(property);
            if (value != null && !property.equals(idProperty)) {
                columnList.add(column);
                valueList.add(value);
                typeList.add(type);
            }else{
                idValue = value;
                idType = type;
                idColumn = column;
            }
        }
        columnNames = columnList.toArray(new String[valueList.size()]);
        columnValue = valueList.toArray(new Object[valueList.size()]);
        columnType = typeList.toArray(new Class[valueList.size()]);
    }

    public String getUpdateSql() {
        if (columnNames == null || columnNames.length == 0) return null;
        String table = entityTable.getTableName();
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ");
        sb.append(table);
        sb.append(" SET ");
        for (String column : columnNames) {
            if(!column.equals(idColumn)) {
                sb.append(column);
                sb.append("=?,");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ");
        return sb.toString();
    }

    public String getInsertSql() {
        if (columnNames == null || columnNames.length == 0) return null;
        String table = entityTable.getTableName();
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ");
        sb.append(table);
        sb.append(" (");
        for (String column : columnNames) {
            sb.append(column);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") values (");
        for (String column : columnNames) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
        return sb.toString();
    }

    public <T> T setId(Number id) throws Exception {
        metaObject.setValue(idProperty, id);
        return (T) metaObject.getBean();
    }

    public String getIdProperty() {
        return idProperty;
    }
    public Object getValue(String property){

        return metaObject.getValue(property);
    }
    public String[] getColumnNames() {
        return columnNames;
    }

    public Object[] getColumnValue() {
        return columnValue;
    }

    public Class<?>[] getColumnType() {
        return columnType;
    }
}
