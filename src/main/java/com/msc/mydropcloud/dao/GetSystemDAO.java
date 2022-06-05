package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public interface GetSystemDAO {

    public List<MyFile> get(UUID uuid);
    public List<MyFile> get(String hash);
}
