/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import config.Config;
import java.util.ArrayList;
/**
 *
 * @author Anik
 */
public class Return {
    private int id;
    private String items;
    private int from_showroom_id;
    private int to_showroom_id;
    
    public Return(int showroomid , String items){
        this.to_showroom_id = showroomid; //the headoffice id.
        this.items = items;
        this.from_showroom_id = Config.SHOWROOM_ID;
        this.id = -1;
    }
    
    public Return(int showroomid , String items , int id){
        this.to_showroom_id = showroomid; //the headoffice id.
        this.items = items;
        this.from_showroom_id = Config.SHOWROOM_ID;
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public int getFrom_showroom_id() {
        return from_showroom_id;
    }

    public int getTo_showroom_id() {
        return to_showroom_id;
    }

    @Override
    public String toString() {
        return "Return{" + "id=" + id + ", items=" + items + ", from_showroom_id=" + from_showroom_id + ", to_showroom_id=" + to_showroom_id + '}';
    }
    
    
    
}
