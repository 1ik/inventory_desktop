/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import config.Config;
import java.sql.ResultSet;
import java.util.ArrayList;
import structures.Expense;
import db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.Item;

/**
 *
 * @author Anik
 */
public class Expense_model {

    public static ArrayList<Expense> getExpenses() {
        ArrayList<Expense> expenseList = new ArrayList();
        ResultSet results = DB.execute_get("select * from expense");
        try {
            while (results.next()) {
                expenseList.add(new Expense(results.getInt("expense_id"), results.getInt("expense_type_id"), results.getFloat("amount"), results.getString("explanation"), results.getString("created_at"), results.getInt("showroom_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Expense_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return expenseList;
    }
    
    public static void delete(int ids []){
        Connection conn = DB.getConnection();
        
        String sql = "DELETE FROM expense WHERE expense_id IN (";
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


    public static void add_expenses(ArrayList<Expense> expenses) {
        String query = "insert into expense (expense_type_id, showroom_id , amount , created_at , explanation) VALUES(?, ?, ?, ? , ?)";
        
         Connection c = DB.getConnection();
        
        try {
            c.setAutoCommit(false);
            String insertTableSQL = "INSERT INTO expense"
                    + "(expense_type_id, showroom_id , amount , created_at , explanation) VALUES"
                    + "(?, ?, ?, ? , ?)";
            
            PreparedStatement preparedStatement = c.prepareStatement(insertTableSQL);
            
            for (Expense expense : expenses) {
                preparedStatement.setInt(1, expense.getExpense_type_id());
                preparedStatement.setInt(2, Config.SHOWROOM_ID);
                preparedStatement.setFloat(3, expense.getAmount());
                preparedStatement.setString(4, expense.getCreated_at());
                preparedStatement.setString(5, expense.getExplanation());
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            c.commit();    
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
    
    public static void main(String args []) {
//        ArrayList<Expense> expenses  = new ArrayList();
//        expenses.add(new Expense(1, 2000, "some expense", "2013-10-5 11:33:44"));
//        expenses.add(new Expense(5, 50, "some expense2", "2013-10-5 11:33:44"));
//        expenses.add(new Expense(4, 1400, "some expense3", "2013-10-5 11:33:44"));
//        expenses.add(new Expense(2, 30, "some expense4", "2013-10-5 11:33:44"));
//        add_expenses(expenses);
    }
}
