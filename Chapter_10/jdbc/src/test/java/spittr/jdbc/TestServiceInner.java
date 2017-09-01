package spittr.jdbc;

import java.sql.SQLException;

public interface TestServiceInner {
    void smokeNested() throws SQLException;

    void smokeRequiresNew() throws SQLException;
}
