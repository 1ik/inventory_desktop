/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import models.Expense_type_model;
import structures.Expense_type;

/**
 *
 * @author Anik
 */
public class Expense_type_view {
    
    /**
     * Takes the jCombobox of Expense type. Gets all the expense type's name from model and reloads the dropdown.
     * @param b jcombobox of Expense_type. 
     */
    public static void reloadView(JComboBox b){
        ArrayList<Expense_type> types = Expense_type_model.get_expense_types();
        DefaultComboBoxModel model = (DefaultComboBoxModel) b.getModel();
        model.removeAllElements();
        for(Expense_type type : types){
            model.addElement(type.getReason());
        }
    }
}
