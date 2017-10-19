package com.github.yml.jdbc.mapper.entity;

import java.lang.annotation.Annotation;

/**
 * Created by yml on 2017/9/2.
 */
public class EntityColumn {
    protected String column;
    protected String property;
    protected Class propertyClass;

    public EntityColumn(String property, String column,Class propertyClass) {
        this.property = property;
        this.column = column;
        this.propertyClass = propertyClass;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(Class propertyClass) {
        this.propertyClass = propertyClass;
    }
}
