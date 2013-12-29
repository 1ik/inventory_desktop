/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Anik
 */
public class Item {
    private int id;
    private int item_id;
    private String type;
    private String size;
    private float sell_price;
 
    private int transfer_id;
    private String color_code;

    public Item(int item_id, String type, String size, float sell_price, String color_code , int transfer_id) {
        this.item_id = item_id;
        this.type = type;
        this.size = size;
        this.sell_price = sell_price;
        this.transfer_id = transfer_id;
        this.color_code = color_code;
    }

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public float getSell_price() {
        return sell_price;
    }


    public int getTransfer_id() {
        return transfer_id;
    }

    public String getColor_code() {
        return color_code;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", item_id=" + item_id + ", type=" + type + ", size=" + size + ", sell_price=" + sell_price + ", transfer_id=" + transfer_id + ", color_code=" + color_code + '}';
    }
    
    
}
