/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.google.gson.Gson;
import db.DB;
import config.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.Memo;
import db.DB;
import java.sql.ResultSet;

/**
 *
 * @author Anik
 */
public class Memo_model {

    public static void add_memo(String items , String changed_items) {
        String query = "insert into memo (added_on , showroom_id, items, returned_items) VALUES(?,?,?,?)";
        Connection connection = (Connection) DB.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(query);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            stmt.setString(1, format.format(new Date()));

            stmt.setInt(2, Config.SHOWROOM_ID);
            stmt.setString(3, items);
            stmt.setString(4, changed_items);
            stmt.executeUpdate();
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Memo_model.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Memo_model.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public static ArrayList<Memo> get() {
        ArrayList<Memo> memos = new ArrayList();

        ResultSet results = DB.execute_get("select * from memo");
        try {
            while (results.next()) {
                
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                memos.add(new Memo(results.getInt("id"), results.getString("added_on"), results.getString("items") , results.getString("returned_items")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Memo_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return memos;
    }
    
    
    public static void main(String args[]) {
        ArrayList<Memo> memos = get();
        for(Memo memo : memos) {
            System.out.println(memo);
        }
    }

    
    
    /**
     * Takes the ids of the memos in memo table. and removes the memo.
     * @param ids the ids of the memos to be removed from memo table.
     */
    
    public static void remove(int[] ids) {
        Connection conn = DB.getConnection();

        String sql = "DELETE FROM memo WHERE id IN (";
        for (int i = 0; i < ids.length; i++) {
            sql += ids[i];
            if (i < ids.length - 1) {
                sql += ",";
            }
        }
        sql += ")";
//        System.out.println(sql);
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Expense_model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
