package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;

/**
 *
 * @author Michael
 */
public interface SaveSystemDAO {

    public void save(MyFile file);
    public void update(MyFile file);
    
}
