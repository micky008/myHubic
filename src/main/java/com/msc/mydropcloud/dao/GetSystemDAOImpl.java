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
        return SaveSystemDAOImpl.uuidFile.get(uuid);
    }

    @Override
    public MyFile get(String hash) {
        return SaveSystemDAOImpl.hashFile.get(hash);
    }

    @Override
    public Set<MyFile> getByParent(UUID uuid) {
        return SaveSystemDAOImpl.uuidParentFile.get(uuid);
    }

   


}
