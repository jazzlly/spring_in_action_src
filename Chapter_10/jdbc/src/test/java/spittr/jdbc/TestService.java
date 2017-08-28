package spittr.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface TestService {
    void smoke(Connection connection) throws SQLException;
    void smoke2() throws SQLException;

}
