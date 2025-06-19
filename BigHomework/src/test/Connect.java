package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Connect {
    Connection conn;


    public Connect() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1/jj";
        String user = "root";
        String password = "2856208614";
        conn = DriverManager.getConnection(url, user, password);
    }


}
