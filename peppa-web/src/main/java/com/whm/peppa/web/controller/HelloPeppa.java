package com.whm.peppa.web.controller;

import com.whm.peppa.web.db.model.Config;
import com.whm.peppa.web.db.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maozhu
 * @date 18/11/26
 */
@RestController
public class HelloPeppa {
    protected static final Logger logger = LoggerFactory.getLogger(HelloPeppa.class);

    @Autowired
    private ConfigService configService;

    @RequestMapping(path = {"/peppa"})
    public Config HelloSpring (){
        logger.info(">>>>>>> hello peppa");
        Config byUnique = configService.findByUnique(1L);


        return byUnique;
    }
}
