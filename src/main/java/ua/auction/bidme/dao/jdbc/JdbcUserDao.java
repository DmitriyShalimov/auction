package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapper;
import ua.auction.bidme.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class JdbcUserDao implements UserDao {
    //todo sql queries to property file
    private final Logger logger = getLogger(getClass());
    private final UserMapper USER_MAPPER = new UserMapper();
    private final String GET_USER_BY_EMAIL_SQL = "SELECT u.id, u.email, u.password from auction.user as u WHERE u.email = ?;";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User get(String email) {
        logger.info("starting query getUserByEmail to db for user {} ...", email);
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL_SQL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = USER_MAPPER.mapRow(resultSet);
                logger.info("query getUserByEmail to db for user {} finished.it took {} ms", email,
                        currentTimeMillis() - start);
                return user;
            }

        } catch (SQLException e) {
            logger.error("an error {} occurred during reading user {} from db", e.getMessage(), email);
            throw new RuntimeException(e);
        }

        return null;
    }

}
