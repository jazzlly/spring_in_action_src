package spittr.jdbc;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;

 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(classes=JdbcMySqlConfig.class)
public class JdbcMySqlTest {

     @Autowired
     DataSource dataSource;

     @Autowired
     TestService testService;

     @Test
     public void smoke() throws Exception {
         Connection connection = null;
         try {
             connection = dataSource.getConnection();
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

     @Test
     public void smokeTransaction() throws Exception {
         // smoke(connection);
         // testService.smoke(connection);
         Runnable runnable = new Runnable() {
             public void run() {
                 try {
                     testService.smoke2();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         };
         Thread thread = new Thread(runnable);
         thread.start();
         thread.join();
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
