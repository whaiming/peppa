package com.whm.peppa.web.db.model;

import java.io.Serializable;
import java.util.Date;

public class ConfigQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 
     */
    private Long id;

    /**
     * 配置文件/配置项
     */
    private Integer type;

    /**
     * 配置文件名/配置项Key名
     */
    private String configKey;

    /**
     * 0 配置文件：文件的内容，1 配置项：配置值
     */
    private String value;

    /**
     * appid
     */
    private Long appId;

    /**
     * 版本
     */
    private String version;

    /**
     * envid
     */
    private Long envId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}