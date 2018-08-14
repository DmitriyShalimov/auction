package ua.auction.bidme.dao.jdbc;

import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapper;
import ua.auction.bidme.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    //todo sql queries to property filr
    private final UserMapper USER_MAPPER = new UserMapper();
    private final String GET_USER_BY_EMAIL_SQL = "SELECT u.id, u.email, u.password from auction.user as u WHERE u.email = ?;";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User get(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL_SQL);) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return USER_MAPPER.mapRow(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
