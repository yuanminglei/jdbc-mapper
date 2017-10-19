package com.github.yml.jdbc.mapper;

import java.util.List;

/**
 * Created by yml on 2017/8/25.
 */
public class PageHelper<T> {

    protected Integer toalSize;
    protected Integer pageSize;
    protected Integer pageNum;
    protected List<T> list;

    public PageHelper(Integer toalSize, Integer pageSize, Integer pageNum, List<T> list) {
        this.toalSize = toalSize;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.list = list;
    }
    public Integer getToalSize() {
        return toalSize;
    }

    public void setToalSize(Integer toalSize) {
        this.toalSize = toalSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
