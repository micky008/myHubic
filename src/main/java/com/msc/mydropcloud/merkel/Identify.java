package com.msc.mydropcloud.merkel;

import com.msc.mydropcloud.GetInstance;
import com.msc.mydropcloud.dao.ConfigDAO;
import com.msc.mydropcloud.dao.DAO;
import com.msc.mydropcloud.dao.GetSystemDAO;
import com.msc.mydropcloud.dao.SaveSystemDAO;
import com.msc.mydropcloud.entity.MyFile;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

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
        this.ssDao = DAO.ssdao;
        this.getDao = DAO.getdao;
        this.configDao = DAO.configdao;
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
        myFile.uuid = GetSystemDAO.ROOT_UUID;
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

    public void addFile(File file) {
        String[] fullPart = getRootFiles(file);
        Set<MyFile> myfiles = getDao.getChildMyFileByUUIDParent(GetSystemDAO.ROOT_UUID);
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
                Set<MyFile> myFiles1 = getDao.getChildMyFileByUUIDParent(tmpfile.uuid);
                tmpfile.hash = merkel.hashMyFiles(myFiles1);
                ssDao.update(tmpfile);
                while (tmpfile.parent != null) {
                    Set<MyFile> myFilesMinus1 = getDao.getChildMyFileByUUIDParent(tmpfile.parent);
                    tmpfile = getDao.get(tmpfile.parent);
                    tmpfile.hash = merkel.hashMyFiles(myFilesMinus1);
                    ssDao.update(tmpfile);
                }
                break;
            }
            find = false;
            myfiles = getDao.getChildMyFileByUUIDParent(tmpfile.uuid);
        }
    }

    public void removeFile(File file) {
        String[] fullPart = getRootFiles(file);
        MyFile tmpfile = getLastMyFile(fullPart);
        if (tmpfile == null) {
            return;
        }
        ssDao.delete(tmpfile);
        updateMerkel(tmpfile);
    }

    public void updateFile(File file) {
        String[] fullPart = getRootFiles(file);
        MyFile tmpfile = getLastMyFile(fullPart);
        if (tmpfile == null) {
            return;
        }
        tmpfile.hash = merkel.hash(file);
        ssDao.update(tmpfile);
        updateMerkel(tmpfile);
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

    private String[] getRootFiles(File file) {
        String next = null;
        if (GetInstance.getIsServer()) {
            file.getAbsolutePath().replace(configDao.getConfig().server.folder + File.separator, "");
        } else {
            file.getAbsolutePath().replace(configDao.getConfig().client.folder + File.separator, "");
        }
        return next.split(Pattern.quote(File.separator));
    }

    private MyFile getLastMyFile(String[] tree) {
        Set<MyFile> myfiles = getDao.getChildMyFileByUUIDParent(GetSystemDAO.ROOT_UUID);
        MyFile tmpfile = null;
        for (String part : tree) {
            for (MyFile myfile : myfiles) {
                if (myfile.endpoint.equals(part)) {
                    tmpfile = myfile;
                    break;
                }
            }
            myfiles = getDao.getChildMyFileByUUIDParent(tmpfile.uuid);
        }
        return tmpfile;
    }

    private void updateMerkel(MyFile lastPoint) {
        while (lastPoint.parent != null) {
            Set<MyFile> myFilesMinus1 = getDao.getChildMyFileByUUIDParent(lastPoint.parent);
            lastPoint = getDao.get(lastPoint.parent);
            lastPoint.hash = merkel.hashMyFiles(myFilesMinus1);
            ssDao.update(lastPoint);
        }
    }

}
