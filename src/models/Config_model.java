/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
import db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import config.Config;
/**
 *
 * @author Anik
 */
public class Config_model {

    public static void initialize() {
        String query = "select * from config";
        ResultSet rset = DB.execute_get(query);
        try {
            while(rset.next()){
                Config.SHOWROOM_NAME = rset.getString("showroom_name");
                Config.SHOWROOM_ID = rset.getInt("showroom_id");
                Config.USERNAME = rset.getString("username");
                Config.PASSWORD = rset.getString("password");                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Config_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(Config.SHOWROOM_NAME);
    }
    
    
    
    public static void insert(String showroomname, int showroom_id, String username, String password) {
        String query = "insert into config"
                + "(showroom_id, showroom_name, username, password)"
                + "VALUES(?,?,?,?)";
        
        Connection con = DB.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, showroom_id);
            ps.setString(2, showroomname);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.execute();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Config_model.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Config_model.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Config_model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    
    
}
