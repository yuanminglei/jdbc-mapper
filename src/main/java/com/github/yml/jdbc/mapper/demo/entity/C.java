package com.github.yml.jdbc.mapper.demo.entity;

import javax.persistence.Table;

/**
 * Created by yml on 2017/9/2.
 */
@Table(name = "test.c")
public class C {
    private int id;
    private String desc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
