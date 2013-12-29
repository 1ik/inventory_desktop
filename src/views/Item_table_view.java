/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import structures.Item;

/**
 *
 * @author Anik
 */
public class Item_table_view {
    
    public static void load_table(JTable table, JTable item_summery_table, ArrayList<Item> items){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        float total_price = 0;
        int total_items = 0;
        for(Item item : items){
            total_price += item.getSell_price();
            total_items += 1;
            model.addRow(new Object[]{ item.getItem_id() , item.getType() , item.getSize() , item.getTransfer_id(), item.getSell_price(),  item.getColor_code()});
        }
        
        DefaultTableModel m = (DefaultTableModel) item_summery_table.getModel();
        m.setRowCount(0);
        m.addRow(new Object [] {total_items, "", "" , "" , total_price , ""});
    }
}
