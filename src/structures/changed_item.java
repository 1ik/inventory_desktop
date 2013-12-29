/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Anik
 */
public class changed_item {
    int item_id;
    String item_type;
    String item_size;
    String price;
    String color;
    String sell_showroom;
    String sell_date;

    public int getItem_id() {
        return item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public String getItem_size() {
        return item_size;
    }

    public String getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public String getSell_showroom() {
        return sell_showroom;
    }

    public String getSell_date() {
        return sell_date;
    }

    @Override
    public String toString() {
        return "changed_item{" + "item_id=" + item_id + ", item_type=" + item_type + ", item_size=" + item_size + ", price=" + price + ", color=" + color + ", sell_showroom=" + sell_showroom + ", sell_date=" + sell_date + '}';
    }
}
