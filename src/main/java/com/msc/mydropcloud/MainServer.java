/*

 */
package com.msc.mydropcloud;

import com.google.protobuf.BoolValue;
import com.google.protobuf.BytesValue;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.File;

/**
 *
 * @author Michael
 */
public class MainServer {
        File dest = new File("C:/tmp/myHubic");

    public void go(String[] args) throws Exception {
        Server s = ServerBuilder.forPort(9998).addService(new LinkServerImpl()).build();
        s.start();
        System.out.println("lecture sur port 9998");
        s.awaitTermination();
    }

    public static void main(String[] args) throws Exception {
        new MainServer().go(args);
    }

    public static class LinkServerImpl extends LinkServiceGrpc.LinkServiceImplBase {

        @Override
        public void send(Link.SendMyMyfile request, StreamObserver<BoolValue> responseObserver) {
            super.send(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        @Override
        public void get(Link.UUID request, StreamObserver<BytesValue> responseObserver) {
            super.get(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        @Override
        public void isUUIDExist(Link.UUID request, StreamObserver<BoolValue> responseObserver) {
            super.isUUIDExist(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
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
        public void hashChange(Link.Hash request, StreamObserver<BoolValue> responseObserver) {
            super.hashChange(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        @Override
        public void imTheFirst(Link.MacAddress request, StreamObserver<BoolValue> responseObserver) {
            super.imTheFirst(request, responseObserver); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

    }

}
