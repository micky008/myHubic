package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class SaveSystemDAOHashMapImpl implements SaveSystemDAO {

    static Map<UUID, Set<MyFile>> uuidParentFile = new HashMap<>();

    @Override
    public void save(MyFile file) {
        if (uuidParentFile.containsKey(file.parent)) {
            uuidParentFile.get(file.parent).add(file);
        } else {
            Set<MyFile> l = new HashSet<>();
            l.add(file);
            uuidParentFile.put(file.parent, l);
        }
    }

    @Override
    public void update(MyFile file) {
        if (!uuidParentFile.containsKey(file.parent)) {
            return;
        }
        if (file.uuid.equals(GetSystemDAOImpl.ROOT_UUID)) {
            uuidParentFile.get(null).add(file);
            return;
        }
        Iterator<MyFile> it = uuidParentFile.get(file.parent).iterator();
        while (it.hasNext()) {
            MyFile next = it.next();
            if (next.endpoint.equals(file.endpoint)) {
                it.remove();
                uuidParentFile.get(file.parent).add(file);
                break;
            }
        }

    }

    @Override
    public void delete(MyFile file) {
        if (file.isDir) {
            uuidParentFile.remove(file.uuid);
        } else {
            Iterator<MyFile> it = uuidParentFile.get(file.parent).iterator();
            while (it.hasNext()) {
                MyFile next = it.next();
                if (next.uuid.equals(file.uuid)) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public void postSave() {
    }

}
