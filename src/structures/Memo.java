/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Anik
 */
import java.util.ArrayList;
import config.Config;

public class Memo {
    private int id;
    private int showroom_id;
    private String added_on;
    private String items;
    private String changed_items;

    public Memo(String pAddingTime , String items , String changed_items) {
        this.showroom_id = Config.SHOWROOM_ID;
        this.added_on = pAddingTime;
        this.items = items;
        this.changed_items = changed_items;
    }
    
    public Memo(int id, String pAddingTime , String items , String changed_items) {
        this.id = id;
        this.showroom_id = Config.SHOWROOM_ID;
        this.added_on = pAddingTime;
        this.items = items;
        this.changed_items = changed_items;
    }

    public int getId() {
        return id;
    }

    public int getShowroom_id() {
        return showroom_id;
    }

    public String getAdded_on() {
        return added_on;
    }

    public String getItems() {
        return items;
    }

    public String getChanged_items() {
        return changed_items;
    }

    @Override
    public String toString() {
        return "Memo{" + "id=" + id + ", showroom_id=" + showroom_id + ", added_on=" + added_on + ", items=" + items + ", changed_items=" + changed_items + '}';
    }
}
