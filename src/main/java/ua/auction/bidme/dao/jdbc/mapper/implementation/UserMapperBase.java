package ua.auction.bidme.dao.jdbc.mapper.implementation;

import ua.auction.bidme.dao.jdbc.mapper.RowMapper;
import ua.auction.bidme.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapperBase implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {


        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        return new User.Builder(email)
                .password(password)
                .build();
    }
}
