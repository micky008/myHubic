package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class GetSystemDAOImpl implements GetSystemDAO {

    @Override
    public List<MyFile> get(UUID uuid) {
        return SaveSystemDAOImpl.uuidFile.get(uuid);
    }

    @Override
    public List<MyFile> get(String hash) {
        return SaveSystemDAOImpl.hashFile.get(hash);

    }


}
