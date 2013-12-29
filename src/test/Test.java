/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gson.Gson;
import db.DB;
import httpClient.HttpRequest;
import httpClient.HttpResponse;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import models.Item_model;
import models.Transfer_model;
import structures.Item;
import structures.Transfer;

/**
 *
 * @author Anik
 */
public class Test {

    public static void main(String args[])throws Exception {
       DB.execute_update("DELETE FROM item");
       DB.execute_update("DELETE FROM config");
       DB.execute_update("DELETE FROM transfer");
       DB.execute_update("DELETE FROM expense");
       DB.execute_update("DELETE FROM expense_type");
       DB.execute_update("DELETE FROM memo");
       DB.execute_update("DELETE FROM returns");
       DB.execute_update("DELETE FROM showrooms");
    }
    
    public static void mysqlinserttest() throws SQLException{
        Connection c = DB.getConnection();
        c.setAutoCommit(false);
        String insertTableSQL = "INSERT INTO item"
                    + "(item_id , type, size, sell_price, barcode, transfer_id) VALUES"
                    + "(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(insertTableSQL);
            for (int i =0; i<100000; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "type"+1);
                preparedStatement.setString(3, "sized");
                preparedStatement.setFloat(4, 332);
                preparedStatement.setString(5, "201310"+i);
                preparedStatement.setInt(6, 66);
                preparedStatement.addBatch();
                /*
                if(i!=0 && i%1000==0){//for every 1000 records, insert as a batch  
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    c.commit();
                }
                */
            }
            preparedStatement.executeBatch();
            c.commit();
            c.close();
            
    }
    
    /*
    
     private static Connection conn=null;
    
      public static Connection getConnection() {
        if (conn == null) {
            connect();
        }
        return conn;
    }

    private static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root&password=");
            System.out.println("connected");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("connection failed");
        }
    }
*/

}
