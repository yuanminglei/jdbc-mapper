package org.yml.jdbc.mapper.sql;


import org.yml.jdbc.mapper.entity.EntityColumn;
import org.yml.jdbc.mapper.entity.EntityTable;
import org.yml.jdbc.mapper.utils.MetaObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yml on 2017/9/2.
 */
public class Criteria {
    //字段是否必须存在
    protected boolean exists;
    //值是否不能为空
    protected boolean notNull;
    //连接条件
    protected String andOr;
    private List<Criterion> criteria;
    private EntityTable entityTable;
    protected Criteria(EntityTable entityTable, CriterionGroup criterionGroup) {
        this.exists = criterionGroup.isExists();
        this.notNull = criterionGroup.isNotNull();
        this.criteria = criterionGroup.getCriterions();
        this.entityTable=entityTable;
    }

    public String column(String property) throws Exception {
        if (property(property) == null) {
            return null;
        }
        return entityTable.getColumn(property);
    }

    private String property(String property) throws Exception {
        if (entityTable.containsProperty(property)) {
            return property;
        } else if (exists) {
            throw new Exception("当前实体类不包含名为" + property + "的属性!");
        } else {
            return null;
        }
    }

    protected void addCriterion(String condition) throws Exception {
        if (condition == null) {
            throw new Exception("Value for condition cannot be null");
        }
        if (condition.startsWith("null")) {
            return;
        }
        criteria.add(new Criterion(condition));
    }

    protected void addCriterion(String condition, Object value, String property) throws Exception {
        if (value == null) {
            if (notNull) {
                throw new Exception("Value for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        criteria.add(new Criterion(condition, value));
    }

    protected void addCriterion(String condition, Object value1, Object value2, String property) throws Exception {
        if (value1 == null || value2 == null) {
            if (notNull) {
                throw new Exception("Between values for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        criteria.add(new Criterion(condition, value1, value2));
    }

    protected void addOrCriterion(String condition) throws Exception {
        if (condition == null) {
            throw new Exception("Value for condition cannot be null");
        }
        if (condition.startsWith("null")) {
            return;
        }
        criteria.add(new Criterion(condition, true));
    }

    protected void addOrCriterion(String condition, Object value, String property) throws Exception {
        if (value == null) {
            if (notNull) {
                throw new Exception("Value for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        criteria.add(new Criterion(condition, value, true));
    }

    protected void addOrCriterion(String condition, Object value1, Object value2, String property) throws Exception {
        if (value1 == null || value2 == null) {
            if (notNull) {
                throw new Exception("Between values for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        criteria.add(new Criterion(condition, value1, value2, true));
    }

    public String getAlias(){
        return entityTable.getAlias();
    }


    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     *
     * @param param 参数对象
     */
    public Criteria orEqualTo(Object param) throws Exception {
        MetaObject<Object> metaObject = MetaObject.forObject(param);
        for (String property : metaObject.properties()) {
            //属性和列对应Map中有此属性
            if (entityTable.containsProperty(property)) {
                Object value = metaObject.getValue(property);
                //属性值不为空
                if (value != null) {
                    orEqualTo(property, value);
                }
            }
        }
        return this;
    }

    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     *
     * @param param 参数对象
     */
    public Criteria andEqualTo(Object param) throws Exception {
        MetaObject<Object> metaObject = MetaObject.forObject(param);
        for (String property : metaObject.properties()) {
            //属性和列对应Map中有此属性
            if (entityTable.containsProperty(property)) {
                Object value = metaObject.getValue(property);
                //属性值不为空
                if (value != null) {
                    andEqualTo(property, value);
                }
            }
        }
        return this;
    }

    public Criteria andIsNull(String property) throws Exception {
        addCriterion(column(property) + " is null");
        return this;
    }

    public Criteria andIsNotNull(String property) throws Exception {
        addCriterion(column(property) + " is not null");
        return this;
    }

    public Criteria andEqualTo(String property, Object value) throws Exception {
        addCriterion(column(property) + " =", value, property(property));
        return this;
    }

    public Criteria andNotEqualTo(String property, Object value) throws Exception {
        addCriterion(column(property) + " <>", value, property(property));
        return this;
    }

    public Criteria andGreaterThan(String property, Object value) throws Exception {
        addCriterion(column(property) + " >", value, property(property));
        return this;
    }

    public Criteria andGreaterThanOrEqualTo(String property, Object value) throws Exception {
        addCriterion(column(property) + " >=", value, property(property));
        return this;
    }

    public Criteria andLessThan(String property, Object value) throws Exception {
        addCriterion(column(property) + " <", value, property(property));
        return this;
    }

    public Criteria andLessThanOrEqualTo(String property, Object value) throws Exception {
        addCriterion(column(property) + " <=", value, property(property));
        return this;
    }

    public Criteria andIn(String property, Iterable values) throws Exception {
        addCriterion(column(property) + " in", values, property(property));
        return this;
    }

    public Criteria andNotIn(String property, Iterable values) throws Exception {
        addCriterion(column(property) + " not in", values, property(property));
        return this;
    }

    public Criteria andBetween(String property, Object value1, Object value2) throws Exception {
        addCriterion(column(property) + " between", value1, value2, property(property));
        return this;
    }

    public Criteria andNotBetween(String property, Object value1, Object value2) throws Exception {
        addCriterion(column(property) + " not between", value1, value2, property(property));
        return this;
    }

    public Criteria andLike(String property, String value) throws Exception {
        addCriterion(column(property) + "  like", value, property(property));
        return this;
    }

    public Criteria andNotLike(String property, String value) throws Exception {
        addCriterion(column(property) + "  not like", value, property(property));
        return this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Criteria andCondition(String condition) throws Exception {
        addCriterion(condition);
        return this;
    }

    /**
     * 手写左边条件，右边用value值
     *
     * @param condition 例如 "length(countryname)="
     * @param value     例如 5
     * @return
     */
    public Criteria andCondition(String condition, Object value) {
        criteria.add(new Criterion(condition, value));
        return this;
    }

    public Criteria orIsNull(String property) throws Exception {
        addOrCriterion(column(property) + " is null");
        return this;
    }

    public Criteria orIsNotNull(String property) throws Exception {
        addOrCriterion(column(property) + " is not null");
        return this;
    }

    public Criteria orEqualTo(String property, Object value) throws Exception {
        addOrCriterion(column(property) + " =", value, property(property));
        return this;
    }

    public Criteria orNotEqualTo(String property, Object value) throws Exception {
        addOrCriterion(column(property) + " <>", value, property(property));
        return this;
    }

    public Criteria orGreaterThan(String property, Object value) throws Exception {
        addOrCriterion(column(property) + " >", value, property(property));
        return this;
    }

    public Criteria orGreaterThanOrEqualTo(String property, Object value) throws Exception {
        addOrCriterion(column(property) + " >=", value, property(property));
        return this;
    }

    public Criteria orLessThan(String property, Object value) throws Exception {
        addOrCriterion(column(property) + " <", value, property(property));
        return this;
    }

    public Criteria orLessThanOrEqualTo(String property, Object value) throws Exception {
        addOrCriterion(column(property) + " <=", value, property(property));
        return this;
    }

    public Criteria orIn(String property, Iterable values) throws Exception {
        addOrCriterion(column(property) + " in", values, property(property));
        return this;
    }

    public Criteria orNotIn(String property, Iterable values) throws Exception {
        addOrCriterion(column(property) + " not in", values, property(property));
        return this;
    }

    public Criteria orBetween(String property, Object value1, Object value2) throws Exception {
        addOrCriterion(column(property) + " between", value1, value2, property(property));
        return this;
    }

    public Criteria orNotBetween(String property, Object value1, Object value2) throws Exception {
        addOrCriterion(column(property) + " not between", value1, value2, property(property));
        return this;
    }

    public Criteria orLike(String property, String value) throws Exception {
        addOrCriterion(column(property) + "  like", value, property(property));
        return this;
    }

    public Criteria orNotLike(String property, String value) throws Exception {
        addOrCriterion(column(property) + "  not like", value, property(property));
        return this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Criteria orCondition(String condition) throws Exception {
        addOrCriterion(condition);
        return this;
    }

    /**
     * 手写左边条件，右边用value值
     *
     * @param condition 例如 "length(countryname)="
     * @param value     例如 5
     * @return
     */
    public Criteria orCondition(String condition, Object value) {
        criteria.add(new Criterion(condition, value, true));
        return this;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    public List<Criterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<Criterion> criteria) {
        this.criteria = criteria;
    }


}
