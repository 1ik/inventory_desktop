/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import com.google.gson.Gson;
import httpClient.HttpRequest;
import java.net.URL;
import config.Config;
import httpClient.HttpResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.Showroom;
/**
 *
 * @author Anik
 */
public class Showroom_sync {
    
    public static Showroom [] get_showrooms(){
        Showroom [] showrooms = null ;
        try {
            HttpRequest request = new HttpRequest(new URL(Config.BASE_URL+"api/get_showrooms_list/"));
            System.out.println("Fetching showroom name lists");
            HttpResponse response = request.get();
            System.out.println("got response");
            String jsonResponse = response.getReponse();
            System.out.println(jsonResponse);
            Gson gson = new Gson();
            showrooms = gson.fromJson(jsonResponse , Showroom[].class);
            
            return showrooms;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Showroom_sync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Showroom_sync.class.getName()).log(Level.SEVERE, null, ex);
        }
        return showrooms;
    }
}
