/*

 */

package com.msc.mydropcloud;

import com.msc.mydropcloud.dao.DAOFactory;
import com.msc.mydropcloud.dao.SaveSystemDAO;
import com.msc.mydropcloud.entity.MyFile;
import com.msc.mydropcloud.merkel.Identify;
import java.io.File;

/**
 *
 * @author Michael
 */
public class MainClientTest {

    public static void main(String[] args) {
        SaveSystemDAO ss = DAOFactory.ssdao;
        Identify identif = new Identify(ss, new File("C:\\Users\\Michael\\Documents\\papiers"));
        MyFile racine = identif.firstScan();
        
        
    }
}
