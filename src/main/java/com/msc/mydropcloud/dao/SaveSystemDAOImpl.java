package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class SaveSystemDAOImpl implements SaveSystemDAO {

    static Map<String, List<MyFile>> hashFile = new HashMap<>();
    static Map<UUID, List<MyFile>> uuidFile = new HashMap<>();

    @Override
    public void save(MyFile file) {
        if (hashFile.containsKey(file.hash)) {
            hashFile.get(file.hash).add(file);
        } else {
            List<MyFile> l = new LinkedList<>();
            l.add(file);
            hashFile.put(file.hash, l);
        }
        if (uuidFile.containsKey(file.uuid)) {
            uuidFile.get(file.uuid).add(file);
        } else {
            List<MyFile> l = new LinkedList<>();
            l.add(file);
            uuidFile.put(file.uuid, l);
        }
    }

}
