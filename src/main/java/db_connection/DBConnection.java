package db_connection;
import java.sql.*;

public class DBConnection {

    public static Connection connection;

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://revatureprojects.cmdqkjlhspzc.us-east-2.rds.amazonaws.com:5432/postgres";
        String username = "postgres";
        String password = "Alje1975!99!";

        connection = DriverManager.getConnection(url,username,password);

        return connection;
    }

}
