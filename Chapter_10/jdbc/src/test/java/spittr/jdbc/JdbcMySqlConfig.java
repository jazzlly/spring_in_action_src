package spittr.jdbc;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spittr.db.SpitterRepository;
import spittr.db.SpittleRepository;
import spittr.db.jdbc.JdbcSpitterRepository;
import spittr.db.jdbc.JdbcSpittleRepository;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"spittr.jdbc"})
@EnableTransactionManagement
public class JdbcMySqlConfig {

  @Bean
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUsername("root");
    dataSource.setPassword("pekall1234");
    dataSource.setUrl("jdbc:mysql://localhost:3306/jdbc_demo?useUnicode=true&characterEncoding=utf-8&autoReconnect=true");
    dataSource.setMaxActive(10);
    dataSource.setMaxIdle(5);
    dataSource.setInitialSize(5);
    dataSource.setValidationQuery("SELECT 1");
    return dataSource;
  }
  
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

//  @Bean
//  public TestService testService(JdbcTemplate jdbcTemplate) {
//      return new TestServiceImpl(jdbcTemplate);
//  }
}
