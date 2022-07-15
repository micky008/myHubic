package com.msc.mydropcloud.dao;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class SaveSystemDAOJsonImpl extends SaveSystemDAOHashMapImpl {

    static Gson gson = new Gson();

    @Override
    public void makeEdge() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File("c:/tmp", "merkel.json"));
            gson.toJson(uuidParentFile, fw);
        } catch (IOException ex) {
            Logger.getLogger(SaveSystemDAOJsonImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(SaveSystemDAOJsonImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
