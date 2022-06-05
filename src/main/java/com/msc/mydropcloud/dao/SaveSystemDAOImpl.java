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
public class SaveSystemDAOImpl implements SaveSystemDAO {

    static Map<String, MyFile> hashFile = new HashMap<>();
    static Map<UUID, Set<MyFile>> uuidParentFile = new HashMap<>();
    static Map<UUID, MyFile> uuidFile = new HashMap<>();

    @Override
    public void save(MyFile file) {
        hashFile.put(file.hash, file);
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
        hashFile.remove(file.uuid);
        hashFile.put(file.hash, file);
//        if (uuidParentFile.containsKey(file.parent)) {
//            uuidParentFile.get(file.parent).add(file);
//        } else {
//            Set<MyFile> l = new HashSet<>();
//            l.add(file);
//            uuidParentFile.put(file.parent, l);
//        }
    }

}
