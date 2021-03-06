package spittr.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ryanjiang on 2017/7/31.
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestServiceInner testServiceInner;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    public TestServiceImpl() {
    }

    public TestServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void smoke(Connection connection) throws SQLException {
       Statement stat = connection.createStatement();

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

    public void smoke1() {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        try {
            jdbcTemplate.execute("DELETE FROM Greetings");
            jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World!')");
            jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 1!')");
            jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 2!')");
            jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 3!')");

            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void smoke2() throws SQLException {
        // jdbcTemplate.execute("DROP TABLE IF EXISTS Greetings");
        // jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Greetings (Message CHAR(20))");
        jdbcTemplate.execute("DELETE FROM Greetings");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World!')");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 1!')");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 2!')");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 3!')");

        // testServiceInner.smokeNested();
        testServiceInner.smokeRequiresNew();
    }
}
