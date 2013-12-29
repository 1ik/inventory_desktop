/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import com.google.gson.Gson;
import httpClient.HttpRequest;
import java.util.ArrayList;
import models.Return_model;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import structures.Return;
import config.Config;
import httpClient.HttpResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anik
 */
public class Returns_sync implements Job {
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("Return : Sync started");
        ArrayList<Return> returns = Return_model.getReturns();
        if(returns.size() <= 0) {
            return;
        }
        
        System.out.println(returns.size() + " returns!!");
        Gson gson = new Gson();
        String json_request = gson.toJson(returns);
        URL url;
        try {
            url = new URL(Config.BASE_URL +"api/transfer_to_showroom/");
            HttpRequest requst = new HttpRequest(url);
            requst.addPostValue("transfers", json_request);
            HttpResponse response = requst.post();
            String responseString = response.getReponse();
            System.out.println("Return sync : response : " + responseString);
            int return_ids[] = gson.fromJson(responseString, int[].class);
            Return_model.removeReturns(return_ids);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Returns_sync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Returns_sync.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Return : sync finished");
    }
    
    
    public static void main (String args []) throws MalformedURLException, IOException {
        
    }
    
}
