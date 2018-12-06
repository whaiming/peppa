//package com.whm.peppa.web.db.dao.impl;
//
//import com.whm.peppa.web.db.dao.ConfigDAO;
//import com.whm.peppa.web.db.model.Config;
//import com.whm.peppa.web.db.model.ConfigQuery;
//import org.mybatis.spring.support.SqlSessionDaoSupport;
//import org.springframework.stereotype.Repository;
//import org.springframework.util.Assert;
//
//import java.util.Date;
//import java.util.List;
//
//@Repository
//public class ConfigDAOImpl extends SqlSessionDaoSupport implements ConfigDAO {
//
//    @Override
//    public int insert(Config config) {
//        config.setCreateTime(new Date());
//        config.setModifyTime(null);
//
//        return this.getSqlSession().insert("Config.INSERT", config);
//    }
//
//    @Override
//    public List<Config> query(ConfigQuery query) {
//        return this.getSqlSession().selectList("Config.QUERY", query);
//    }
//
//    @Override
//    public Integer count(ConfigQuery query) {
//        return this.getSqlSession().selectOne("Config.COUNT", query);
//    }
//
//    @Override
//    public Config findByUnique( Long id  ) {
//        Assert.notNull(id, "id不能为空");
//
//       return this.getSqlSession().selectOne("Config.FIND_BY_UNIQUE", id);
//    }
//
//    @Override
//    public int updateByUnique(Config updateParam) {
//        Assert.notNull(updateParam,"更新参数不能为空");
//        Assert.notNull(updateParam.getId(), "id不能为空");
//
//        updateParam.setCreateTime(null);
//        updateParam.setModifyTime(new Date());
//
//        return this.getSqlSession().update("Config.UPDATE_BY_UNIQUE", updateParam);
//    }
//
//}