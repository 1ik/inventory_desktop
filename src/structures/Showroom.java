/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Anik
 */
public class Showroom {
    
    public int id;
    public String name;
    
    public Showroom(int showroom_id , String name){
        this.id = showroom_id;
        this.name = name;
    }

    public int getShowroom_id() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Showroom{" + "id=" + id + ", name=" + name + '}';
    }
    
    
    
}
