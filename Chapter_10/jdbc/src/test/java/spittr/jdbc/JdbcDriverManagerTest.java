package spittr.jdbc;

import org.junit.Test;

import java.sql.*;

// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes=JdbcConfig.class)
public class JdbcDriverManagerTest {

    @Test
    public void smoke() throws Exception {
        String url = "jdbc:mysql://localhost:3306/jdbc_demo?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";
        String username = "root";
        String password = "pekall1234";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            // smoke(connection);
            // preparedStatement(connection);
            smokeTransaction(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected void smoke(Connection conn) throws SQLException {
        Statement stat = conn.createStatement();

        stat.executeUpdate("DROP TABLE IF EXISTS Greetings");
        stat.executeUpdate("CREATE TABLE IF NOT EXISTS Greetings (Message CHAR(20))");
        stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");

        ResultSet result = stat.executeQuery("SELECT * FROM Greetings");
        while (result.next()) {
            System.out.println(result.getString(1));
            System.out.println(result.getString("Message"));
        }
        // stat.executeUpdate("DROP TABLE Greetings");
        // stat.close();

        stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, JDBC!')");
        result = stat.executeQuery("SELECT * FROM Greetings");
        while (result.next()) {
            System.out.println(result.getString(1));
            System.out.println(result.getString("Message"));
        }
    }

    private void preparedStatement(Connection connection) throws SQLException {
        final String publisherQuery = "SELECT Books.Price, Books.Title FROM Books, Publishers"
                + " WHERE Books.Publisher_Id = Publishers.Publisher_Id AND Publishers.Name = ?";

        PreparedStatement stat = connection.prepareStatement(publisherQuery);
        stat.setString(1, "Prentice Hall"); // 参数从1开始

        ResultSet resultSet = stat.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
        }
    }

    private void smokeTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);

        Statement stat = connection.createStatement();
//        stat.executeUpdate("DROP TABLE IF EXISTS Greetings");
//        stat.executeUpdate("CREATE TABLE IF NOT EXISTS Greetings (Message CHAR(20))");
//        stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");
//        stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World 1!')");

        stat.addBatch("DROP TABLE IF EXISTS Greetings");
        stat.addBatch("CREATE TABLE IF NOT EXISTS Greetings (Message CHAR(20))");
        stat.addBatch("INSERT INTO Greetings VALUES ('Hello, World!')");
        stat.addBatch("INSERT INTO Greetings VALUES ('Hello, World 1!')");
        stat.executeBatch();

        Savepoint savepoint = connection.setSavepoint();
        stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World 2!')");

        ResultSet result = stat.executeQuery("SELECT * FROM Greetings");
        while (result.next()) {
            System.out.println(result.getString(1));
            System.out.println(result.getString("Message"));
        }
        connection.rollback(savepoint);
        connection.commit();
    }
}
