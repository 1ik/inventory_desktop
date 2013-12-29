/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import com.google.gson.Gson;
import httpClient.HttpRequest;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Expense_model;
import structures.Expense;
import config.Config;
import httpClient.HttpResponse;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Responsible for syncing the expense table from local database to web.
 *
 * @author Anik
 */
public class Expense_sync {

    public static void startsynching(int milliseconds) {
        Scheduler d = new Scheduler(new TaskExecuter() {
            @Override
            public void execute() {
                ArrayList<Expense> expense_list = Expense_model.getExpenses();
                System.out.println("executing sync for expenses....");
                if (expense_list.size() > 0) {
                    System.out.println("synching...."+expense_list.size()+" items");
                    Gson gson = new Gson();
                    String json = gson.toJson(expense_list);
                    try {
                        
                        HttpRequest request = new HttpRequest(new URL(Config.BASE_URL + "api/request_new_expense"));
                        System.out.println(json);
                        request.addPostValue("expenses", json);
                        HttpResponse response = request.post();
                        json = response.getReponse();
                        System.out.println(json);
                        
                        int [] ids = gson.fromJson(json, int[].class);
                        Expense_model.delete(ids);
                        System.out.println("removing "+ids.length+" items from local table");
                        
                    } catch (IOException ex) {
                        //Logger.getLogger(Expense_sync.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("Problem with connection : Expense_sync.java");
                    }
                }
            }
        }, milliseconds);

        Thread s = new Thread(d);
        s.start();
    }
}
