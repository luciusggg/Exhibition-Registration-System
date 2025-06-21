package exhibition;

import java.sql.*;
import javax.swing.*;

public class DBConnect {
    public static Connection getConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String url = "jdbc:ucanaccess://D:/NetbeanProjects/VUE_Exhibition.accdb";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Error: " + e.getMessage());
            return null;
        }
    }
}