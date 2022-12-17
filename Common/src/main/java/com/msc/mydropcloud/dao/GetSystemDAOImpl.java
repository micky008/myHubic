package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class GetSystemDAOImpl implements GetSystemDAO {

    @Override
    public MyFile get(UUID uuid) {
        if (uuid.equals(ROOT_UUID) || uuid == null) {
            Iterator<MyFile> it = SaveSystemDAOHashMapImpl.uuidParentFile.get(null).iterator();
            it.hasNext();
            return it.next();
        }
        MyFile res = recurs(ROOT_UUID, uuid);
        return res;
    }

    @Override
    public Set<MyFile> getChildMyFileByUUIDParent(UUID uuid) {
        return SaveSystemDAOHashMapImpl.uuidParentFile.get(uuid);
    }

    private MyFile recurs(UUID root, UUID uuidToSearch) {
        Set<MyFile> files = SaveSystemDAOHashMapImpl.uuidParentFile.get(root);
        MyFile res = null;
        for (MyFile file : files) {
            if (file.isDir) {
                if (file.uuid.equals(uuidToSearch)) {
                    res = file;
                    break;
                }
                res = recurs(file.uuid, uuidToSearch);
            } else {
                if (file.uuid.equals(uuidToSearch)) {
                    res = file;
                    break;
                }
            }
        }
        return res;
    }

}
