package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public interface GetSystemDAO {

    public static final UUID ROOT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    
    /**
     * Get all files from uuid.
     * @param uuid
     * @return 
     */
    public Set<MyFile> getChildMyFileByUUIDParent(UUID uuid);
    
    /**
     * If UUID dosn't exsit return null.
     * @param uuid
     * if uuid == null or ROOT_UUID it's return the root folder
     * @return 
     */
    public MyFile get(UUID uuid);
}
