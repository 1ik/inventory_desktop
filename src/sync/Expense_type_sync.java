/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import com.google.gson.Gson;
import httpClient.HttpRequest;
import config.Config;
import httpClient.HttpResponse;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.Expense_type;

/**
 *
 * @author Anik
 */
public class Expense_type_sync {
    
    public Expense_type_sync(){
    }
    
    public static Expense_type [] DownloadExpense_types(){
        try {
            HttpRequest req = new HttpRequest(new URL(Config.BASE_URL+"api/get_expense_types"));
            HttpResponse response = req.get();
            String json = response.getReponse();
            Gson gson = new Gson();
            Expense_type types [] = gson.fromJson(json, Expense_type[].class);
            return types;
            
        } catch (IOException ex) {
//            Logger.getLogger(Expense_type_sync.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Problem with connection : Expense_type_sync.java");
        }
        return null;
    }
}


