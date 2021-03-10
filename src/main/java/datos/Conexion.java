package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class Conexion {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3300/controlclientes"
            + "?useSSL=false"
            + "&useTimezone=true"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "arreolari7";
    private static BasicDataSource dataSource;
    
    public static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new BasicDataSource();
            dataSource.setUrl(JDBC_URL);
            dataSource.setUsername(JDBC_USER);
            dataSource.setPassword(JDBC_PASS);
            dataSource.setInitialSize(50);
        }
        return dataSource;
    }
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = Conexion.getDataSource().getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return connection;
    }
    
    public static void close(PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch(SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    public static void close(ResultSet resultSet){
        try {
            resultSet.close();
        } catch(SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    public static void close(Statement statement){
        try {
            statement.close();
        } catch(SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    public static void close(Connection connection) {
        try {
            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
