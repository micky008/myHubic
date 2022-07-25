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

//       File dest = new File("C:\\Users\\Michael\\Documents\\papiers");
//        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9998).build();
        //     TestHelloServiceGrpc.TestHelloServiceBlockingStub blockingStub = TestHelloServiceGrpc.newBlockingStub(channel);
        //     HelloMessage message = HelloMessage.newBuilder().setMessage("bonjour Connau").build();
        //    Test.HelloResponse resp = blockingStub.setFeature(message);
        //    System.out.println(resp.getResp());
        Identify identif = new Identify(new File("C:\\Users\\Michael\\Documents\\papiers"));
        //list(racine);
        File file = new File("C:\\Users\\Michael\\Documents\\papiers\\dentiste\\img1.webp");
        File file2 = new File("c:\\tmp\\img1.webp");
        File file3 = new File("c:\\tmp\\img2.webp");
        File file4 = new File("C:\\Users\\Michael\\Documents\\papiers\\osteo\\img2.webp");
        File file5 = new File("c:\\tmp\\img3.webp");
        File file6 = new File("C:\\Users\\Michael\\Documents\\papiers\\mdph\\img3.webp");
        File file7 = new File("c:\\tmp\\img4.webp");
        if (file2.exists()) {
            if (file.exists()) {
                file.delete();
            }
            file2.renameTo(file);
        }
        MyFile racine = identif.firstScan();
        DAOFactory.ssdao.makeEdge();
        file.renameTo(file2);
        identif.removeFile(file);
        //list(racine);
        if (file4.exists()) {
            if (file3.exists()) {
                file3.delete();
            }
            file4.renameTo(file3);
        }
        file3.renameTo(file4);
        identif.addFile(file4);
        
        file6.renameTo(file7);
        file5.renameTo(file6);
        
        identif.updateFile(file6);
        
        DAOFactory.ssdao.makeEdge();
        
        
        

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
