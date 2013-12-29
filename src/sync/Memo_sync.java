/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import com.google.gson.Gson;
import httpClient.HttpRequest;
import java.net.URL;
import java.util.ArrayList;
import models.Memo_model;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import structures.Memo;
import config.Config;
import httpClient.HttpResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anik
 */
public class Memo_sync implements Job{

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("starting to sync memo");
        
        ArrayList<Memo> memos = Memo_model.get();
        
        if(memos.size() >0){
            try {
                System.out.println(memos.size() + " items found !!!");
                Gson gson = new Gson();
                String memo_json = gson.toJson(memos);
                System.out.println(memo_json);
                
                System.out.println("sending request to webserver ....");
                HttpRequest request = new HttpRequest(new URL(Config.BASE_URL + "api/request_new_memo/"));
                request.addPostValue("memos", memo_json);
                HttpResponse server_response = request.post();
                String response_string = server_response.getReponse();
                System.out.println(response_string);
                
                //we should receive a response of array containing the memoid we sent.
                int [] memo_ids = gson.fromJson(response_string, int[].class);
                System.out.println("removing memos from memo table...");
                Memo_model.remove(memo_ids);
                
            } catch (IOException ex) {
                Logger.getLogger(Memo_sync.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
            System.out.println("No items in the memo table found!!");
        }
        
        System.out.println("memo table checking ended");
    }
    
    
    public static void main(String args []) {
        ArrayList<Memo> memos = Memo_model.get();
        for(Memo memo : memos) {
            System.out.println(memo);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(memos));
    }
}
