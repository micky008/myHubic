/*

 */
package com.msc.mydropcloud;

import com.msc.mydropcloud.dao.DAOFactory;
import com.msc.mydropcloud.entity.MyFile;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author Michael
 */
public class MainClientTest {

    public static void main(String[] args) throws SQLException {

//       File dest = new File("C:\\Users\\Michael\\Documents\\papiers");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9998).build();

   //     TestHelloServiceGrpc.TestHelloServiceBlockingStub blockingStub = TestHelloServiceGrpc.newBlockingStub(channel);

   //     HelloMessage message = HelloMessage.newBuilder().setMessage("bonjour Connau").build();

    //    Test.HelloResponse resp = blockingStub.setFeature(message);
        
    //    System.out.println(resp.getResp());

        //Identify identif = new Identify(dest);
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
