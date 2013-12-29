/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Anik
 */
public class Transfer {
    public int id; //just the local id;
    public int transfer_id; //the id that has been created from web
    public int showroom_id;
    public String created_at;
    public int total_items;

    public Transfer(int id, int transfer_id, int showroom_id, String created_at , int total_items) {
        this.id = id;
        this.transfer_id = transfer_id;
        this.showroom_id = showroom_id;
        this.created_at = created_at;
        this.total_items = total_items;
    }

    public int getId() {
        return id;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public int getShowroom_id() {
        return showroom_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getTotal_items() {
        return total_items;
    }
    

    @Override
    public String toString() {
        return "Transfer{" + "id=" + id + ", transfer_id=" + transfer_id + ", showroom_id=" + showroom_id + ", created_at=" + created_at + ", total_items=" + total_items + '}';
    }
    
    
}
