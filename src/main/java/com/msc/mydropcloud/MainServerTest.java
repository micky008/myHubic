/*

 */
package com.msc.mydropcloud;

import com.msc.mydropcloud.dao.DAOFactory;
import com.msc.mydropcloud.entity.MyFile;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author Michael
 */
public class MainServerTest {

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {

//       File dest = new File("C:\\Users\\Michael\\Documents\\papiers");
        Server s = ServerBuilder.forPort(9998).addService(new GreeterImpl()).build();
        s.start();
        System.out.println("lecture sur port 9998");
        s.awaitTermination();
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

    static class GreeterImpl extends TestHelloServiceGrpc.TestHelloServiceImplBase {

        
//        @Override
//        public void setFeature(HelloMessage req, StreamObserver<HelloResponse> responseObserver) {
//            HelloResponse reply = HelloResponse.newBuilder().setResp("Hello " + req.getMessage()).build();
//            responseObserver.onNext(reply);
//            responseObserver.onCompleted();
//        }
    }

}
