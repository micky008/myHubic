/*

 */
package com.msc.mydropcloud;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.msc.mydropcloud.dao.DAO;
import com.msc.mydropcloud.dao.GetSystemDAO;
import com.msc.mydropcloud.entity.MyFile;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Michael
 */
public class MainClient {

    private static LinkServiceGrpc.LinkServiceBlockingStub linkGrpc;
//    private static LinkServiceGrpc.LinkServiceStub linkAsyncGrpc;

    public static void main(String[] args) throws Exception {
        new MainClient().go(args);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(DAO.configdao.getConfig().common.host, DAO.configdao.getConfig().common.port).build();
        linkGrpc = LinkServiceGrpc.newBlockingStub(channel);
//        linkAsyncGrpc = LinkServiceGrpc.newStub(channel);

    }

    public String getMyName() throws Exception {

        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();
        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }
        return String.join("-", hexadecimal);
    }

    public void go(String args[]) throws Exception {

        MyFile root = DAO.getdao.get(GetSystemDAO.ROOT_UUID);
        Link.Hash hash = Link.Hash.newBuilder().setHash(root.hash).build();
        BoolValue hashChanged = linkGrpc.hashChange(hash);
        if (!hashChanged.getValue()) {
            return;
        }

        Link.MacAddress myName = Link.MacAddress.newBuilder().setAddress(getMyName()).build();
        BoolValue imFirst = linkGrpc.imTheFirst(myName);

        if (imFirst.getValue()) {
            recursImFirst(root, DAO.configdao.getConfig().client.folder);
        } else {
            recursNotFirst(root, DAO.configdao.getConfig().client.folder);
        }
    }

    private void recursNotFirst(MyFile root, String folder) {
        Link.UUID rootUUID = Link.UUID.newBuilder().setUuid(root.uuid.toString()).build();
        Link.ArrayMyFile arrMf = linkGrpc.getFileTreeFromServer(rootUUID);
        List<Link.MyFile> myfiles = arrMf.getMyfilesList();
        for (Link.MyFile lMf : myfiles) {
            MyFile mf = new MyFile(lMf);
            MyFile getMyFile = DAO.getdao.get(mf.uuid); //is this file exist ?
            if ((getMyFile != null && getMyFile.hash.equals(mf.hash))) { //yes the file exsit and the hash have not moved
                continue;
            }
            if (mf.isDir) {
                folder = folder + "/" + mf.endpoint;
                recursNotFirst(mf, folder);
            } else {
                getNewFile(folder, lMf);
            }
        }
    }

    private void getNewFile(String folder, Link.MyFile lmf) {
        Link.UUID uuid = Link.UUID.newBuilder().setUuid(lmf.getUuid()).build();
        BytesValue byV = linkGrpc.get(uuid);
        File file = new File(folder, lmf.getEndpoint());
        OutputStream os = null;
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            IOUtils.copyLarge(byV.getValue().newInput(), os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recursImFirst(MyFile rootFile, String folder) {
        Link.ArrayMyFile.Builder bArrMf = Link.ArrayMyFile.newBuilder();
        Set<MyFile> myfiles = DAO.getdao.getChildMyFileByUUIDParent(rootFile.uuid);
        int pos = 0;
        for (MyFile myFile : myfiles) {
            bArrMf.addMyfiles(pos++, myFile.convert());
        }
        Link.ArrayMyFile arrMf = bArrMf.build();
        Link.ArrayUUID uuids = Link.ArrayUUID.getDefaultInstance();

        while (uuids != null) {
            uuids = linkGrpc.sendFileTreeToServer(arrMf);
            for (Link.UUID uuid : uuids.getUuidList()) {
                MyFile getMyFile = DAO.getdao.get(UUID.fromString(uuid.getUuid())); //is this file exist ?
                if (getMyFile.isDir) {
                    recursImFirst(getMyFile, folder + "/" + getMyFile.endpoint);
                } else {
                    File file = new File(folder, getMyFile.endpoint);
                    try {
                        InputStream is = new FileInputStream(file);
                        ByteArrayOutputStream os = new ByteArrayOutputStream((int) file.length());
                        IOUtils.copyLarge(is, os);
                        is.close();
                        ByteString bs = ByteString.copyFrom(os.toByteArray());
                        Link.SendMyMyfile smmf = Link.SendMyMyfile.newBuilder().
                                setMyfile(getMyFile.convert()).
                                setPathName(folder).
                                setContent(bs).
                                build();
                        linkGrpc.send(smmf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
