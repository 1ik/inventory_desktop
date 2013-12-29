/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.Showroom;

/**
 *
 * @author insane
 */
public class showroom_model {

    public static void insert(Showroom[] showrooms) {
        DB.execute_update("DELETE FROM showrooms");

        Connection c = DB.getConnection();

        try {
            c.setAutoCommit(false);
            String insertTableSQL = "INSERT INTO showrooms"
                    + "(name , showroom_id) VALUES"
                    + "(?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(insertTableSQL);

            for (Showroom showroom : showrooms) {
                preparedStatement.setString(1, showroom.getName());
                preparedStatement.setInt(2, showroom.getShowroom_id());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            c.commit();
            c.close();

        } catch (SQLException ex) {
            Logger.getLogger(Expense_type_model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Expense_type_model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static ArrayList<Showroom> get() {
        ArrayList<Showroom> showroom_list = new ArrayList();
        String query = "select * from showrooms";
        ResultSet results = DB.execute_get(query);
        try {
            while(results.next()) {
                showroom_list.add(new Showroom(results.getInt("showroom_id"), results.getString("name")));
            }
            results.getStatement().getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(showroom_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return showroom_list;
    }
    
    
    public static int getId (String showroom_name ) {
        
        String sql = "select showroom_id from showrooms where name = '"+showroom_name+"'";
        ResultSet resSet = DB.execute_get(sql);
        int showroom_id = -1;
        try {
            while(resSet.next()) {
                showroom_id = resSet.getInt("showroom_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(showroom_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return showroom_id;
    }
    
    
    public static void main (String args []) {
        ArrayList<Showroom> list = get();
    }
}
