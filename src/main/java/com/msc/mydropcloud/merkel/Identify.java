package com.msc.mydropcloud.merkel;

import com.msc.mydropcloud.dao.SaveSystemDAO;
import com.msc.mydropcloud.entity.MyFile;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class Identify {

    private final SaveSystemDAO ssDao;
    private final File file;
    private final Merkel merkel = new Merkel();

    public Identify(SaveSystemDAO ssDao, File folder) {
        this.ssDao = ssDao;
        if (folder.isFile()) {
            this.file = folder.getParentFile();
        } else {
            this.file = folder;
        }
    }

    public MyFile firstScan() {
        MyFile myFile = new MyFile();
        myFile.endpoint = this.file.getName();
        myFile.parent = null;
        myFile.uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        myFile.isDir = true;        
        myFile.hash = recurs(myFile.uuid, this.file);
        ssDao.save(myFile);
        return myFile;
    }

    private String recurs(UUID uuid, File dir) {
        List<MyFile> list = new LinkedList<>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                MyFile myFile = new MyFile();
                myFile.endpoint = file.getName();
                myFile.uuid = UUID.randomUUID();
                myFile.parent = uuid;
                myFile.isDir = true;
                myFile.hash = recurs(myFile.uuid, file);
                ssDao.save(myFile);
                list.add(myFile);
            } else {
                MyFile myFile = new MyFile();
                myFile.endpoint = file.getName();
                myFile.uuid = UUID.randomUUID();
                myFile.parent = uuid;
                myFile.hash = merkel.hash(file);
                ssDao.save(myFile);
                list.add(myFile);
            }
        }        
        return merkel.hashMyFiles(list);
        
    }

}
