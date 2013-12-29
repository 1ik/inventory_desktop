/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import views.initializing_form;
import config.Config;
import models.Config_model;

/**
 *
 * @author Anik
 */
public class Main {
    public static void main(String args []){
        //initialize the applicatoin with loading the config from database.
        Config_model.initialize();
        
        if(Config.USERNAME != null){
            //load the login form.
            System.out.println("loading login form");
            new LoginForm().setVisible(true);
            
        }else{
            initializing_form form = new initializing_form();
            form.setVisible(true);
        }
    }
}
