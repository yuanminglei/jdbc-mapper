package com.github.yml.jdbc.mapper.annotation;

/**
 * Created by yml on 2017/9/2.
 */
public  enum JoinType {
    COMMA(","),
    LEFT_OUTER_JOIN("LEFT JOIN"),
    RIGHT_OUTER_JOIN("RIGHT JOIN");

    public final String name;

    private JoinType(String name) {
        this.name = name;
    }

    public String toString(JoinType joinType) {
        return joinType.name;
    }
}
