package com.github.yml.jdbc.mapper.utils;

import org.springframework.beans.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yml on 2017/9/6.
 */
public class MetaObject<T> {
    private static ConversionService conversionService = new DefaultConversionService();
    private List<String> properties = new ArrayList<>();
    private BeanWrapperImpl bw;
    private T t;

    public T getBean() {
        return t;
    }

    public void setValue(String property, Object value) {
        if(!properties.contains(property))
             properties.add(property);
        bw.setPropertyValue(property,value);
    }

    public Object getValue(String property) {
        //set,get方法返回值类型，必须与属性类型一样，即使基本类型与封装类型的区别也会报错，待优化
        return bw.getPropertyValue(property);
    }

    public List<String> properties() {
        return properties;
    }

    public static<T> MetaObject<T> forClass(Class<T> c) {
        return forObject(BeanUtils.instantiateClass(c));
    }
    public static<T> MetaObject forObject(T t) {
        MetaObject<T> metaObject = new MetaObject<>();
        metaObject.initBeanWrapper(t);
        return metaObject;
    }
    private  void initBeanWrapper(T o){
        t = o;
        bw =(BeanWrapperImpl) PropertyAccessorFactory.forBeanPropertyAccess(t);
        bw.setConversionService(conversionService);
        PropertyDescriptor[] propertyDescriptors =  bw.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String property = propertyDescriptor.getName();
            if(!"class".equals(property)) {
                properties.add(property);
            }
        }
    }

}
