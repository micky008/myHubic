package com.msc.mydropcloud.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msc.mydropcloud.GetInstance;
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

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void postSave() {
        FileWriter fw = null;
        try {
            int pos = 0;
            File file = null;
            if (GetInstance.getIsServer()) {
                file = new File(DAO.configdao.getConfig().server.confFolder, "merkel-" + pos + ".json");
            } else {
                file = new File(DAO.configdao.getConfig().client.confFolder, "merkel-" + pos + ".json");
            }
            while (file.exists()) {
                pos++;
                if (GetInstance.getIsServer()) {
                    file = new File(DAO.configdao.getConfig().server.confFolder, "merkel-" + pos + ".json");
                } else {
                    file = new File(DAO.configdao.getConfig().client.confFolder, "merkel-" + pos + ".json");
                }
            }
            fw = new FileWriter(file);
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
