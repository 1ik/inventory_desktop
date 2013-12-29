package db;
/**
 * Responsible for building a new connection to database and retuns the new
 * connection.
 *
 * @author Anik
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    /**
     * Returns noting. Used for only insert, update and Delete.
     *
     * @param query the query String
     */
    public static void execute_update(String query) {
        Statement statement = getStatement();
        try {
            statement.executeUpdate(query);
            statement.getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Queries the database and returns result. Used for retrieving.
     *
     * @param query
     * @return ResultSet the query results.
     */
    public static ResultSet execute_get(String query) {
        Statement statement = getStatement();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    /**
     * Returns a statement for query
     * @return 
     */

    public static Statement getStatement() {
        try {
            Statement statement = getConnection().createStatement();
            return statement;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
        }
        return null;
    }
    
    /**
     *  Returns a new connection.
     * @return Connection
     */
    
    public static Connection getConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = null;
            //connection = DriverManager.getConnection("jdbc:sqlite:d://wamp/www/jf_java/jfdb.sqlite");
            connection = DriverManager.getConnection("jdbc:sqlite:jfdb.sqlite");
            return connection;
        } catch (Exception ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
