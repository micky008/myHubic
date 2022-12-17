package com.msc.mydropcloud.dao;

import com.msc.mydropcloud.entity.MyFile;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.jdbc.OrientJdbcConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class SaveSystemDAOOriendbImpl implements SaveSystemDAO {

    private Connection conn;

    public SaveSystemDAOOriendbImpl() {
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "root");
        info.put("db.usePool", "true"); // USE THE POOL
        info.put("db.pool.min", "10");   // MINIMUM POOL SIZE
        OGlobalConfiguration.RID_BAG_EMBEDDED_TO_SBTREEBONSAI_THRESHOLD.setValue(-1);
        try {
            conn = (OrientJdbcConnection) DriverManager.getConnection("jdbc:orient:remote:localhost/merkel", info);
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSystemDAOOriendbImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save(MyFile file) {
        try {
            String query = "INSERT INTO File (endpoint, isDir, uuid, parent, hash) VALUES (?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, file.endpoint);
            ps.setBoolean(2, file.isDir);
            ps.setString(3, file.uuid.toString());
            ps.setString(4, file.parent != null ? file.parent.toString() : "null");
            ps.setString(5, file.hash);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(SaveSystemDAOOriendbImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void update(MyFile file) {
    }

    @Override
    public void delete(MyFile file) {

    }

    
    @Override
    public void postSave() {
        try {
            String query = "Select parent, uuid from File";
            String edge = "CREATE EDGE FROM (SELECT FROM File WHERE uuid = ?) TO (SELECT FROM File WHERE uuid = ?)";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            PreparedStatement ps = conn.prepareStatement(edge);
            while (rs.next()) {
                ps.setString(1, rs.getString(1));
                ps.setString(2, rs.getString(2));
                ps.execute();
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SaveSystemDAOOriendbImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
