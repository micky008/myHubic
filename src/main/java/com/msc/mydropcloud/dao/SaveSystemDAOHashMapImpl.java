package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class SaveSystemDAOHashMapImpl implements SaveSystemDAO {

    static Map<UUID, Set<MyFile>> uuidParentFile = new HashMap<>();
    static Map<UUID, MyFile> uuidFile = new HashMap<>();

    @Override
    public void save(MyFile file) {
        uuidFile.put(file.uuid, file);
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
        MyFile oldFile = uuidFile.get(file.uuid);
        oldFile.hash = file.hash;
        uuidFile.put(oldFile.uuid, oldFile);
    }

    @Override
    public void delete(MyFile file) {
        if (file.isDir) {
            Set<MyFile> files = uuidParentFile.get(file.uuid);
            for (MyFile f : files) {
                uuidFile.remove(f.uuid);
            }
            uuidParentFile.remove(file.uuid);
        }        
        uuidFile.remove(file.uuid);
    }

    @Override
    public void makeEdge() {
        
    }

}
