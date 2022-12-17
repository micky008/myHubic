package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.Config;


/**
 *
 * @author Michael
 */
public class ConfigDAOImpl implements ConfigDAO {

    private final Config config = new Config();

    @Override
    public Config getConfig() {
        return config;
    }

}
