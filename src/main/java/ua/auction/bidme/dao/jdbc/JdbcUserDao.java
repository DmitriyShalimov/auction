package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapper;
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapperBase;
import ua.auction.bidme.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class JdbcUserDao implements UserDao {
    private final Logger logger = getLogger(getClass());
    private final UserMapper USER_MAPPER = new UserMapper();
    private final UserMapperBase USER_MAPPER_ID = new UserMapperBase();
    private final String GET_USER_BY_EMAIL_SQL;
    private final String GET_USER_BY_ID_SQL;
    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource, Properties queryProperties) {
        this.dataSource = dataSource;
        this.GET_USER_BY_EMAIL_SQL = queryProperties.getProperty("GET_USER_BY_EMAIL_SQL");
        this.GET_USER_BY_ID_SQL = queryProperties.getProperty("GET_USER_BY_ID_SQL");
    }

    @Override
    public User get(String email) {
        logger.info("starting query getUserByEmail to db for user {} ...", email);
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL_SQL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            User user = USER_MAPPER.mapRow(resultSet);
            logger.info("query getUserByEmail to db for user {} finished.it took {} ms", email, currentTimeMillis() - start);
            return user;
        } catch (SQLException e) {
            logger.error("an error {} occurred during reading user {} from db", e.getMessage(), email);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(int id) {
        logger.info("starting query getUserById to db for user id {} ...", id);
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            User user = USER_MAPPER_ID.mapRow(resultSet);
            logger.info("query getUserById to db for user {} finished.it took {} ms", id, currentTimeMillis() - start);
            return user;
        } catch (SQLException e) {
            logger.error("an error {} occurred during reading user {} from db", e.getMessage(), id);
            throw new RuntimeException(e);
        }
    }
}
