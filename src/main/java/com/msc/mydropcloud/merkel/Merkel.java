package com.msc.mydropcloud.merkel;

import com.msc.mydropcloud.entity.MyFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class Merkel {

    private MessageDigest shaDigest;

    public Merkel() {
        try {
            shaDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Merkel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String hash(File file) {
        try {
            return getFileChecksum(shaDigest, new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Merkel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "error";
    }

    public String hash(List<File> files) {
        if (files.isEmpty()) {
            return "vide";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (File file : files) {
            try {
                baos.write(hash(file).getBytes());
            } catch (IOException ex) {
                Logger.getLogger(Merkel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return getFileChecksum(shaDigest, new ByteArrayInputStream(baos.toByteArray()));
    }

    public String hashMyFiles(Set<MyFile> files) {
        if (files.isEmpty()) {
            return "vide";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (MyFile file : files) {
            try {
                baos.write(file.hash.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(Merkel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return getFileChecksum(shaDigest, new ByteArrayInputStream(baos.toByteArray()));
    }

    private static String getFileChecksum(MessageDigest digest, InputStream is) {
        try {
            //Get file input stream for reading the file content           

            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            //Read file data and update in message digest
            while ((bytesCount = is.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            };

            //close the stream; We don't need it now.
            is.close();

            //Get the hash's bytes
            byte[] bytes = digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //return complete hash
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
