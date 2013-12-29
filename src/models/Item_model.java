/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import structures.Item;
import db.DB;
import static db.DB.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anik
 */
public class Item_model {
    
    
    /**
     * Taks a transfer id retunrs all the items assosiated with that transferid.
     * @param transfer_id the transferid
     * @return ArrayList<Item>.
     */
    public static ArrayList<Item> get(int transfer_id){
        ArrayList<Item> item_lists = new ArrayList();
        String query = "select * from item where transfer_id = "+transfer_id;
//        System.out.println(query);
        ResultSet results = DB.execute_get(query);
        try {
            while(results.next()){
                item_lists.add(new Item(results.getInt("item_id"), results.getString("type"),results.getString("size") , 
                        results.getFloat("sell_price"), results.getString("color_code") , results.getInt("transfer_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Item_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item_lists;
    }

    public static void insert(Item[] items) {
        
        Connection c = DB.getConnection();
        
        try {
            c.setAutoCommit(false);
            String insertTableSQL = "INSERT INTO item"
                    + "(item_id , type, size, sell_price, color_code , transfer_id) VALUES"
                    + "(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(insertTableSQL);
            
            for (Item item : items) {
                preparedStatement.setInt(1, item.getItem_id());
                preparedStatement.setString(2, item.getType());
                preparedStatement.setString(3, item.getSize());
                preparedStatement.setFloat(4, item.getSell_price());
                preparedStatement.setString(5, item.getColor_code());
                preparedStatement.setInt(6, item.getTransfer_id());
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            c.commit();
            c.close();
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Expense_type_model.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Expense_type_model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    
    
    public static void delete(int ids []){
        Connection conn = DB.getConnection();
        
        String sql = "DELETE FROM item WHERE item_id IN (";
        for(int i =0; i < ids.length; i++){
            sql += ids[i];
            if(i<ids.length-1){
                sql+=",";
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
   
    public static Item get_single_item(int item_id) {
        String query = "select * from item where item_id = '"+item_id+"'";
        ResultSet result = DB.execute_get(query);
        Item item = null;
        try {
            while(result.next()){
                item = new Item(result.getInt("item_id"), result.getString("type") , result.getString("size"),
                        result.getFloat("sell_price"), result.getString("color_code") ,result.getInt("transfer_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Item_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }
}
