package com.msc.mydropcloud.utils;

import com.msc.mydropcloud.Link;
import com.msc.mydropcloud.entity.MyFile;

/**
 *
 * @author Michael
 */
public class Convert {

    public static Link.MyFile convert(MyFile myfile) {
        Link.MyFile res = Link.MyFile.newBuilder()
                .setEndpoint(myfile.endpoint)
                .setHash(myfile.hash)
                .setIsDir(myfile.isDir)
                .setParent(myfile.parent.toString())
                .setUuid(myfile.uuid.toString())
                .build();
        return res;
    }
    
}
