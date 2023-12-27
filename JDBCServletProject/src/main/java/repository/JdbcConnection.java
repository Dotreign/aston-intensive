package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

    public static Connection getConnection() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/links", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
