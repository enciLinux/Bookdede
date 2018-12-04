package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    private static Connection connection;

    public MySQLConnect() throws SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost/bookdede_db", "root", "");
    }

    public static Connection getInstance() {
        return connection;
    }
}
