package com.msc.mydropcloud.merkel;

import com.msc.mydropcloud.dao.ConfigDAO;
import com.msc.mydropcloud.dao.DAOFactory;
import com.msc.mydropcloud.dao.GetSystemDAO;
import com.msc.mydropcloud.dao.SaveSystemDAO;
import com.msc.mydropcloud.entity.MyFile;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class Identify {

    private final SaveSystemDAO ssDao;
    private final GetSystemDAO getDao;
    private final ConfigDAO configDao;
    private final File file;
    private final Merkel merkel = new Merkel();

    public Identify(File folder) {
        this.ssDao = DAOFactory.ssdao;
        this.getDao = DAOFactory.getdao;
        this.configDao = DAOFactory.configdao;
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
        myFile.hash = recursFirstScan(myFile.uuid, this.file);
        ssDao.save(myFile);
        return myFile;
    }

    private String recursFirstScan(UUID uuid, File dir) {
        Set<MyFile> list = new HashSet<>();
        for (File file : dir.listFiles()) {
            MyFile myFile;
            if (file.isDirectory()) {
                myFile = createFolder(uuid, file);
            } else {
                myFile = createFile(uuid, file);
            }
            ssDao.save(myFile);
            list.add(myFile);
        }
        return merkel.hashMyFiles(list);
    }

    public String addFile(File file) {
        UUID root = UUID.fromString("00000000-0000-0000-0000-000000000001");
        String next = file.getAbsolutePath().replace(configDao.getConfig().rootFolder, "");
        String[] fullPart = next.split(File.pathSeparator);
        Set<MyFile> myfiles = getDao.getByParent(root);
        MyFile tmpfile = null;
        boolean find = false;
        for (String part : fullPart) {
            for (MyFile myfile : myfiles) {
                if (myfile.endpoint.equals(part)) {
                    tmpfile = myfile;
                    find = true;
                    break;
                }
            }
            if (tmpfile == null) { //never appends except if it's root
                continue;
            }
            if (!find) {// find = false so it's a new folder or a new file
                MyFile myFile;
                if (file.isDirectory()) {
                    myFile = createFolder(tmpfile.uuid, file);
                } else {
                    myFile = createFile(tmpfile.uuid, file);
                }
                ssDao.save(myFile); //newFile was attached to tmpFile
                //now we modify hash in desc mode
                Set<MyFile> myFiles1 = getDao.getByParent(tmpfile.uuid);
                tmpfile.hash = merkel.hashMyFiles(myFiles1);
                ssDao.update(tmpfile);
                while (tmpfile.parent != null) {
                    Set<MyFile> myFilesMinus1 = getDao.getByParent(tmpfile.parent);
                    tmpfile = getDao.get(tmpfile.parent);
                    tmpfile.hash = merkel.hashMyFiles(myFilesMinus1);
                    ssDao.update(tmpfile);
                }
                break;
            }
            find = false;
            myfiles = getDao.getByParent(tmpfile.uuid);
        }
        return null;
    }

    private MyFile createFolder(UUID parent, File file) {
        MyFile myFile = new MyFile();
        myFile.endpoint = file.getName();
        myFile.uuid = UUID.randomUUID();
        myFile.parent = parent;
        myFile.isDir = true;
        myFile.hash = recursFirstScan(myFile.uuid, file);
        return myFile;
    }

    private MyFile createFile(UUID parent, File file) {
        MyFile myFile = new MyFile();
        myFile.endpoint = file.getName();
        myFile.uuid = UUID.randomUUID();
        myFile.parent = parent;
        myFile.hash = merkel.hash(file);
        return myFile;
    }
}
