package spittr.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JdbcMySqlConfig.class)
public class JdbcMySqlTransactionTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    TestService testService;

    @Test
    public void smokeTransaction() throws Exception {
        testService.smoke2();
    }
}
