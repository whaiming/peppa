package com.whm.peppa.web.db.service.impl;

import com.whm.peppa.web.db.dao.ConfigDao;
import com.whm.peppa.web.db.model.Config;
import com.whm.peppa.web.db.model.ConfigQuery;
import com.whm.peppa.web.db.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public List<Config> query(ConfigQuery query) {
        return configDao.query(query);
    }

    @Override
    public Integer count(ConfigQuery query) {
        return configDao.count(query);
    }

    @Override
    public Config findByUnique( Long id  ) {
        return configDao.findByUnique( id  );
    }

    @Override
    public int updateByUnique(Config updateParam) {
        return configDao.updateByUnique(updateParam);
    }

    @Override
    public int insert(Config config) {


        int num = configDao.insert(config);

        if (num > 0) {
            config.setId(config.getId());
        }

        return num;
    }

}