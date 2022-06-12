/*

 */
package com.msc.mydropcloud;

import com.msc.mydropcloud.dao.DAOFactory;
import com.msc.mydropcloud.entity.MyFile;
import com.msc.mydropcloud.merkel.Identify;
import java.io.File;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author Michael
 */
public class MainClientTest {

    public static void main(String[] args) throws SQLException {

        File dest = new File("C:\\Users\\Michael\\Documents\\papiers");

        Identify identif = new Identify(dest);
//        MyFile racine = identif.firstScan();
//        DAOFactory.ssdao.makeEdge();
//        //list(racine);
//        File file = new File("C:\\Users\\Michael\\Documents\\papiers\\dentiste\\img1.webp");
//        File file2 = new File("C:\\Users\\Michael\\Documents\\papiers\\dentiste\\img1.webp");
//        file.renameTo(new File("c:\\tmp\\img1.webp"));
//        identif.removeFile(file2);
//        list(racine);
    }

    private static void list(MyFile file) {
        System.out.println(file.endpoint + (file.isDir ? "/\t" : "") + "\t" + file.parent + "\t" + file.uuid + "\t" + file.hash);
        Set<MyFile> racine = null;
        racine = DAOFactory.getdao.getByParent(file.uuid);
        for (MyFile f : racine) {
            if (f.isDir) {
                list(f);
            } else {
                System.out.println(f.endpoint + (f.isDir ? "/\t" : "") + "\t" + f.parent + "\t" + f.uuid + "\t" + f.hash);
            }
        }
    }

}
