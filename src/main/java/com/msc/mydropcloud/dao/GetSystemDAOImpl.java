package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class GetSystemDAOImpl implements GetSystemDAO {

    @Override
    public MyFile get(UUID uuid) {
        return SaveSystemDAOHashMapImpl.uuidFile.get(uuid);
    }

    @Override
    public Set<MyFile> getByParent(UUID uuid) {
        return SaveSystemDAOHashMapImpl.uuidParentFile.get(uuid);
    }

   


}
