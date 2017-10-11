package org.yml.jdbc.mapper.demo.entity;

import javax.persistence.Table;

/**
 * Created by yml on 2017/9/2.
 */
@Table(name = "test.b")
public class B {
    private int id;
    private int cid;
    private String desc;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

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
