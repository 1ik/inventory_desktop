/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import structures.Transfer;

/**
 *
 * @author Anik
 */
public class Transfer_tab_view {

    public static boolean locked = false;

    public static synchronized void load_table(JTable table, ArrayList<Transfer> transfers) {

        DefaultTableModel tablemodel = (DefaultTableModel) table.getModel();
        tablemodel.setRowCount(0);
        for (Transfer t : transfers) {
            tablemodel.addRow(new Object[]{t.getTransfer_id(), t.getTotal_items(), t.created_at});
        }
    }
}
