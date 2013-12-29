/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import com.google.gson.Gson;
import models.Transfer_model;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import config.Config;
import httpClient.HttpRequest;
import httpClient.HttpResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Item_model;
import structures.Item;
import structures.TransferList;

/**
 *
 * @author Anik
 */
public class Transfer_sync {

    private static boolean requestsent = false;

    public static void sync() {
                System.out.println("Trying to update the transfe....");

        if (requestsent) {

            System.out.println("Aborting transfer sync request is already sent.");

        } else {
            requestsent = true;
            int latest_transfer_id = Transfer_model.get_last_transfer_id();
            System.out.println(latest_transfer_id);
            String url = Config.BASE_URL + "api/get_new_items/" + Config.SHOWROOM_ID + "/";
            if (latest_transfer_id != -1) {
                url += latest_transfer_id;
                System.out.println("Requesting new transfer with transfer id = " + latest_transfer_id);
            }
            System.out.println(url);

            try {

                HttpRequest request = new HttpRequest(new URL(url));
                HttpResponse response = request.get();
                String json_response = response.getReponse();
                System.out.println("Response received...");
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
            } finally {
                System.out.println("Transfer synching ended ...");
                requestsent = false;
            }
        }
    }
    
    public static void main(String args []) throws IOException {
        String url = "http://jhinukfashion.com/inventory/api/get_new_items/2/";
        HttpRequest request = new HttpRequest(new URL(url));
        HttpResponse response = request.get();
        String json_response = response.getReponse();
        System.out.println("Response received...");
        Gson gson = new Gson();
//        System.err.println(json_response);
        TransferList transfer_list = gson.fromJson(json_response, TransferList.class);
//        Item_model.insert(transfer_list.items);
        for(Item item : transfer_list.items) {
            System.err.println(item);
        }
    }
    
}
