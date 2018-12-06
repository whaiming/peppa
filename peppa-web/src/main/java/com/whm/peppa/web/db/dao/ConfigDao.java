package com.whm.peppa.web.db.dao;


import com.whm.peppa.web.db.model.Config;
import com.whm.peppa.web.db.model.ConfigQuery;

import java.util.List;

public interface ConfigDao {

	/**
     * 新增一条记录
     */
    int insert(Config config);

    /**
     * 根据查询参数查询数据
     *
     * @param query 查询参数
     */
    List<Config> query(ConfigQuery query);

    /**
     * 根据查询参数查询数据总量
     *
     * @param query 查询参数
     */
    Integer count(ConfigQuery query);

    /**
     * 根据唯一键查询
     */
    Config findByUnique(Long id);

    /**
     * 根据唯一键更新数据
     *
     * @param updateParam 更新参数
     */
    int updateByUnique(Config updateParam);

}