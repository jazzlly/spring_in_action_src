package spittr.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * Created by ryanjiang on 2017/8/28.
 */
@Service
public class TestServiceInnerImpl implements TestServiceInner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.NESTED)
    public void smokeNested() throws SQLException {
        // 这里有异常
        jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, world abc trx, fjkslbjkpkalkdfj;aksdlfj')");
        // jdbcTemplate.execute("SELECT * FROM Greetings");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void smokeRequiresNew() throws SQLException {
        // 这里有异常
        // jdbcTemplate.execute("INSERT INTO Greetings VALUES ('Hello, trx')");
        jdbcTemplate.execute("SELECT * FROM Greetings");
    }
}
