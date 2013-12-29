/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Anik
 */
public class Expense_type {
    private int id;
    private String reason;
    public Expense_type(int id , String reason){
        this.id = id;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    
    @Override
    public String toString() {
        return "Expense_type{" + "id=" + id + ", reason=" + reason + '}';
    }
    
    
    
}
