package com.github.yml.jdbc.mapper.demo.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by yml on 2017/9/18.
 */
@Table(name = "ycd_cash_db.t_app_message")
public class AppMessage  {
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型：0 个人消息，1 系统消息
     */
    private Integer type;

    /**
     * 子类型：0其他，1认证审核，2借款审核，3下款，4还款成功，5到期提醒，6逾期提醒
     */
    private Integer subtype;

    /**
     * 来源：0 系统触发，1 后台定制，2 后台单发
     */
    private Integer source;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取用户id
     *
     * @return uid - 用户id
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置用户id
     *
     * @param uid 用户id
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取类型：0 个人消息，1 系统消息
     *
     * @return type - 类型：0 个人消息，1 系统消息
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型：0 个人消息，1 系统消息
     *
     * @param type 类型：0 个人消息，1 系统消息
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取子类型：0其他，1认证审核，2借款审核，3下款，4还款成功，5到期提醒，6逾期提醒
     *
     * @return subtype - 子类型：0其他，1认证审核，2借款审核，3下款，4还款成功，5到期提醒，6逾期提醒
     */
    public Integer getSubtype() {
        return subtype;
    }

    /**
     * 设置子类型：0其他，1认证审核，2借款审核，3下款，4还款成功，5到期提醒，6逾期提醒
     *
     * @param subtype 子类型：0其他，1认证审核，2借款审核，3下款，4还款成功，5到期提醒，6逾期提醒
     */
    public void setSubtype(Integer subtype) {
        this.subtype = subtype;
    }

    /**
     * 获取来源：0 系统触发，1 后台定制，2 后台单发
     *
     * @return source - 来源：0 系统触发，1 后台定制，2 后台单发
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置来源：0 系统触发，1 后台定制，2 后台单发
     *
     * @param source 来源：0 系统触发，1 后台定制，2 后台单发
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}