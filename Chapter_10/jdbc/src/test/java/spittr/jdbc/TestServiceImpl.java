package spittr.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public TestServiceImpl() {
    }

    public TestServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
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

    @Transactional
    public void smoke2() throws SQLException {
        jdbcTemplate.execute("DROP TABLE IF EXISTS Greetings");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Greetings (Message CHAR(20))");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World!')");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 1!')");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 2!')");
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, World 3!')");
    }
}
