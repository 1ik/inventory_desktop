/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import models.Config_model;

/**
 *
 * @author Anik
 */
public class Config {
    public static String DATABASE = "databasename";
//    public static String BASE_URL = "http://localhost/inventory/";
    public static String BASE_URL = "http://jhinukfashion.com/inventory/";
    public static  String SHOWROOM_NAME = "Orkid Plaza store";
    public static  int SHOWROOM_ID = 2;
    public static String USERNAME = null;
    public static String PASSWORD = null;
    

    public static void initialize_app(String showroomname, int showroom_id, String username, String password) {
        Config_model.insert(showroomname , showroom_id , username, password);
        
    }
    
    
   
}
