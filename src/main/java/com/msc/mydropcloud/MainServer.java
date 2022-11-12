/*

 */
package com.msc.mydropcloud;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.msc.mydropcloud.dao.DAO;
import static com.msc.mydropcloud.dao.GetSystemDAO.ROOT_UUID;
import com.msc.mydropcloud.entity.MyFile;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Michael
 */
public class MainServer {

    public void go(String[] args) throws Exception {
        GetInstance.setIsServer(true);
        Server s = ServerBuilder.forPort(DAO.configdao.getConfig().common.port).addService(new LinkServerImpl()).build();
        s.start();
        System.out.println("lecture sur port " + DAO.configdao.getConfig().common.port);
        s.awaitTermination();
    }

    public static void main(String[] args) throws Exception {
        new MainServer().go(args);
    }

    private class LinkServerImpl extends LinkServiceGrpc.LinkServiceImplBase {

        @Override
        public void send(Link.SendMyMyfile request, StreamObserver<BoolValue> responseObserver) {
            File file = null;
            if (request.getPathName().isEmpty()) {
                file = new File(DAO.configdao.getConfig().server.folder, request.getMyfile().getEndpoint());
            } else {
                String endpath = request.getPathName();
                if (!request.getPathName().endsWith("/")) {
                    endpath += "/";
                }
                file = new File(DAO.configdao.getConfig().server.folder, endpath + request.getMyfile().getEndpoint());
            }
            ByteString bs = request.getContent();
            try {
                FileUtils.writeByteArrayToFile(file, bs.toByteArray());
                responseObserver.onNext(BoolValue.of(true));
                responseObserver.onCompleted();
            } catch (Exception e) {
                e.printStackTrace();
                responseObserver.onError(e);
            }
        }

        @Override
        public void get(Link.UUID request, StreamObserver<BytesValue> responseObserver) {
            MyFile mf = DAO.getdao.get(UUID.fromString(request.getUuid()));
            String fileName = getFileFillPathName(mf);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BytesValue bv = null;
            try {
                byte bys[] = FileUtils.readFileToByteArray(new File(DAO.configdao.getConfig().server.folder, fileName));
                bv = BytesValue.newBuilder().setValue(ByteString.copyFrom(bys)).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            responseObserver.onNext(bv);
            responseObserver.onCompleted();
        }

        @Override
        public void isUUIDExist(Link.UUID request, StreamObserver<BoolValue> responseObserver) {
            UUID isUUIDExist = UUID.fromString(request.getUuid());
            MyFile isExist = DAO.getdao.get(isUUIDExist);
            responseObserver.onNext(BoolValue.of(isExist != null));
            responseObserver.onCompleted();
        }

        @Override
        public void hashChange(Link.Hash request, StreamObserver<BoolValue> responseObserver) {
            MyFile root = DAO.getdao.get(ROOT_UUID);
            responseObserver.onNext(BoolValue.of(root.hash.equals(request.getHash())));
            responseObserver.onCompleted();
        }

        @Override
        public void sendFileTreeToServer(Link.ArrayMyFile request, StreamObserver<Link.ArrayUUID> responseObserver) {
            super.sendFileTreeToServer(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        @Override
        public void getFileTreeFromServer(Link.UUID request, StreamObserver<Link.ArrayMyFile> responseObserver) {
            super.getFileTreeFromServer(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        @Override
        public void imTheFirst(Link.MacAddress request, StreamObserver<BoolValue> responseObserver) {
            super.imTheFirst(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        private String getFileFillPathName(MyFile mf) {
            if (mf.parent == null) {
                return mf.endpoint;
            }
            MyFile reverse = mf;
            List<String> lst = new LinkedList<>();
            while (reverse.parent != null) {
                lst.add(reverse.endpoint);
                reverse = DAO.getdao.get(reverse.parent);
            }
            Collections.reverse(lst);
            return String.join("/", lst);
        }

    }

}
