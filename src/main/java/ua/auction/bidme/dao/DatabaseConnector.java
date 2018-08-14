package ua.auction.bidme.dao;

import org.postgresql.jdbc2.optional.PoolingDataSource;
import ua.auction.bidme.util.PropertyReader;

import javax.sql.DataSource;
import java.util.Properties;

public class DatabaseConnector {

    private static DatabaseConnector connector;

    private final DataSource dataSource;

    private DatabaseConnector() {
        this.dataSource = configureDb();
    }

    public static DatabaseConnector getInstance() {
        if (connector == null) {
            synchronized (DatabaseConnector.class) {
                if (connector == null) {
                    connector = new DatabaseConnector();
                }
            }
        }
        return connector;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private DataSource configureDb() {
        PropertyReader reader = new PropertyReader("database-connection.properties");
        Properties properties = reader.readProperties();
        PoolingDataSource dataSource = new PoolingDataSource();
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));
        dataSource.setDatabaseName(properties.getProperty("db_name"));
        return dataSource;
    }
}
