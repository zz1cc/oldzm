package com.zm.platform.yw.manage.entity.system.task;

import com.zm.platform.yw.base.BaseEntity;

public class Task extends BaseEntity {
    //名称
    private String name;
    //表达式
    private String cronExpression;
    //调用目标 包名+类名+方法名
    private String invokeName;
    //状态 1.执行一次 2.执行一次 3.不执行
    private Integer status;
    //并发执行
    private Integer concurrent;
    //分组
    private String groupName;
    //备注
    private String remarks;

    public Task() {

    }

    public String getInvokeName() {
        return invokeName;
    }

    public void setInvokeName(String invokeName) {
        this.invokeName = invokeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
