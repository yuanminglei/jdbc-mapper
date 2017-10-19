package com.github.yml.jdbc.mapper.entity;

import com.github.yml.jdbc.mapper.sql.MysqlTools;
import com.github.yml.jdbc.mapper.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by yml on 2017/9/7.
 */
public class EntityInfo {

    protected Class entityClass;
    protected String tableName;
    protected String idProperty;
    //key:property,value:EntityColumn
    protected Map<String,EntityColumn>  propertyKeyMap= new HashMap<>();
    //key:column,value:EntityColumn
    protected HashMap<String,EntityColumn> columnKeyMap = new HashMap<>();
    protected List<String> properties = new ArrayList<>();
    protected List<String> columns = new ArrayList<>();
    protected List<EntityColumn> entityColumns = new ArrayList<>();
    protected String insertSqlAllColumns;

    public String getTableName() {
        return tableName;
    }

    public EntityInfo(Class entityClass) {
        this.entityClass = entityClass;
        Field[] superFields = entityClass.getSuperclass().getDeclaredFields();
        generateColumns(superFields);
        Field[] fields = entityClass.getDeclaredFields();
        generateColumns(fields);
        setTableName(entityClass);
        generateInsertSqlAllColumns();
    }
    private void setTableName(Class<?> entityClass){
        Annotation[] classAnnotation = entityClass.getAnnotations();
        for(Annotation cAnnotation : classAnnotation){
            if(Table.class.equals(cAnnotation.annotationType())){
                tableName = ((Table)cAnnotation).name();
            }
        }
        if(tableName==null || tableName.isEmpty()){
            //类名转换,驼峰变下划线
            tableName = StringUtils.camel2Underline(entityClass.getName());
        }

    }
    private void generateInsertSqlAllColumns(){
        if (columns == null || columns.size() == 0) return;
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        sb.append("insert into ");
        sb.append(tableName);
        sb.append(" (");
        for (EntityColumn  entityColumn: entityColumns) {
            String column = entityColumn.getColumn();
            if(!column.equals(idProperty)
                    && !column.equals("create_time") && !column.equals("update_time") ) {
                sb.append(column);
                sb.append(",");
                sb2.append("?,");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") values (");
        sb.append(sb2);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
        insertSqlAllColumns = sb.toString();
    }
    private void generateColumns(Field[] fields){
        for (Field field : fields) {
            String property = field.getName();
            Class fieldClass =  field.getType();
            String column =null;
            //连表查询的实体
            if (field.isAnnotationPresent(Column.class)) {
                Annotation annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    Column columnAnno = (Column) annotation;
                    column = columnAnno.name();
                }
            }
            if(StringUtils.isEmpty(column)){
                //属性名转列名，驼峰转下划线
                column = StringUtils.camel2UnderlineForLowerCase(property);
            }
            if (field.isAnnotationPresent(Id.class)) {
                Annotation annotation = field.getAnnotation(Id.class);
                if (annotation != null) {
                    idProperty = property;
                }
            }
            //mysql 关键字转义`column`
            column = MysqlTools.keyWordEscapeWithColumn(column);
            EntityColumn entityColumn = new EntityColumn(property,column,fieldClass);
            columnKeyMap.put(column,entityColumn);
            propertyKeyMap.put(property,entityColumn);
            columns.add(column);
            entityColumns.add(entityColumn);
            properties.add(property);
        }
    }

    public List<EntityColumn> getEntityColumns() {
        return entityColumns;
    }

    public String getInsertSqlAllColumns() {
        return insertSqlAllColumns;
    }

    public EntityColumn getEntityColumnByColumn(String column) {
        return columnKeyMap.get(column);
    }
    public EntityColumn getEntityColumnByProperty(String property) {
        return propertyKeyMap.get(property);
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public boolean containsProperty(String property) {
        return propertyKeyMap.containsKey(property);
    }
    public boolean containsColumn(String column) {
        return columnKeyMap.containsKey(column);
    }
}
