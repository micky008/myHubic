package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public interface GetSystemDAO {

    public Set<MyFile> getByParent(UUID uuid);
    public MyFile get(UUID uuid);
}
