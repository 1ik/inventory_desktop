/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import structures.Item;

/**
 *
 * @author Anik
 */
public class Return_table_item_view {
    
   public static void add_item(JTable return_item_table, Item item){
       DefaultTableModel model = (DefaultTableModel) return_item_table.getModel();
       model.addRow(new Object[]{item.getItem_id(), item.getType() , item.getSize() , item.getSell_price() , item.getTransfer_id() , item.getColor_code()});
   }
   
   public static int[] get_items_from_table(JTable return_item_table){
       int [] items = null;
       items = new int [return_item_table.getRowCount()];
       DefaultTableModel model = (DefaultTableModel) return_item_table.getModel();
       
       for(int i=0; i<items.length; i++){
           items[i] = (int) model.getValueAt(i, 0);
       }
       return items;
   }
}
