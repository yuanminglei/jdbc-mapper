package org.yml.jdbc.mapper.demo.entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yml on 2017/9/2.
 */
@Table(name = "test.a")
public class A {
    @Id
    private Integer id;
    private Integer bid;
    private Integer cid;
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
