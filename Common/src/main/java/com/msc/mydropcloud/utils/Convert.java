package com.msc.mydropcloud.utils;

import com.msc.mydropcloud.Link;
import com.msc.mydropcloud.entity.MyFile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class Convert {

    public static Link.MyFile convertMyfileToLinkMyFile(MyFile myfile) {
        Link.MyFile res = Link.MyFile.newBuilder()
                .setEndpoint(myfile.endpoint)
                .setHash(myfile.hash)
                .setIsDir(myfile.isDir)
                .setParent(myfile.parent.toString())
                .setUuid(myfile.uuid.toString())
                .build();
        return res;
    }

    public static MyFile convertLinkMyFileToMyFile(Link.MyFile lmf) {
        MyFile myf = new MyFile();
        myf.endpoint = lmf.getEndpoint();
        myf.hash = lmf.getHash();
        myf.isDir = lmf.getIsDir();
        myf.parent = UUID.fromString(lmf.getParent());
        myf.uuid = UUID.fromString(lmf.getUuid());
        return myf;
    }

    public static List<MyFile> convertLinkMyFileToMyFile(List<Link.MyFile> llmyfiles) {
        List<MyFile> myfiles = new ArrayList<>(llmyfiles.size());
        for (Link.MyFile lmy : llmyfiles) {
            myfiles.add(convertLinkMyFileToMyFile(lmy));
        }
        return myfiles;
    }

}
