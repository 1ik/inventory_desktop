/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.google.gson.Gson;
import config.Config;
import java.sql.ResultSet;
import java.util.ArrayList;
import structures.Transfer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import db.DB;
import static db.DB.getConnection;
import httpClient.HttpRequest;
import httpClient.HttpResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import structures.TransferList;
/**
 *
 * @author Anik
 */
public class Transfer_model {
    
    
    
    public static ArrayList<Transfer> get(){
        ArrayList<Transfer> transfer_list = new ArrayList();
        String query = "SELECT * from transfer order by created_at desc";
        ResultSet resultset = DB.execute_get(query);
        try {
            while(resultset.next()){
                transfer_list.add(new Transfer(resultset.getInt("id"), resultset.getInt("transfer_id"), resultset.getInt("showroom_id"), resultset.getString("created_at"), resultset.getInt("total_items")));
            }            
        } catch (SQLException ex) {
            Logger.getLogger(Transfer_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transfer_list;
    }
    
    

    public static int get_last_transfer_id() {
        String query = "select id,transfer_id from transfer order by transfer_id desc limit 1";
        int transfer_id = -1;
        ResultSet results = DB.execute_get(query);
        try {
            while(results.next()){
                transfer_id = results.getInt("transfer_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transfer_model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transfer_id;
    }
    

    public static void insert(Transfer[] transfer_ids) {
        
        Connection c = DB.getConnection();
        try {
            c.setAutoCommit(false);
            PreparedStatement ps = c.prepareStatement("");

            String insertTableSQL = "INSERT INTO transfer"
                    + "(transfer_id , showroom_id, created_at, total_items) VALUES"
                    + "(?,?,?,?)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertTableSQL);
            for (Transfer transfer : transfer_ids) {
                preparedStatement.setInt(1, transfer.getTransfer_id());
                preparedStatement.setInt(2, transfer.getShowroom_id());
                preparedStatement.setString(3, transfer.getCreated_at());
                preparedStatement.setInt(4, transfer.getTotal_items());
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
    
    public static void main(String args[]){
        
        
        int latest_transfer_id = Transfer_model.get_last_transfer_id();
        System.out.println(latest_transfer_id);
        String url = Config.BASE_URL + "api/get_new_items/" + Config.SHOWROOM_ID + "/";
        if (latest_transfer_id != -1) {
            url += latest_transfer_id;
            System.out.println("Requesting new transfer with transfer id = "+latest_transfer_id);
        }
        System.out.println(url);

        try {
            HttpRequest request = new HttpRequest(new URL(url));
            HttpResponse response = request.get();
            String json_response = response.getReponse();
            System.out.println(json_response);
            Gson gson = new Gson();
            TransferList transfers = gson.fromJson(json_response, TransferList.class);

            if (transfers.transfer_ids.length > 0) {
                //insert the transfer information in the database.
                System.out.println("updating transfer table..");
                Transfer_model.insert(transfers.transfer_ids);

                //insert the item informations as well..
                System.out.println("updating item table..");
                Item_model.insert(transfers.items);
            }

        } catch (MalformedURLException ex) {
//            Logger.getLogger(Transfer_sync.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("problem with connectin ... Transfer_sync.java");
        } catch (IOException ex) {
            //Logger.getLogger(Transfer_sync.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("problem with connectin ... Transfer_sync.java");
        }finally{
            
            System.out.println("Transfer synching ended ...");
        }
        
        
        
    }
    
    
    
}
