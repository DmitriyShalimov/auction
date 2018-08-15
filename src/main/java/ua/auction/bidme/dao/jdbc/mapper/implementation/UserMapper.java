package ua.auction.bidme.dao.jdbc.mapper.implementation;

import ua.auction.bidme.dao.jdbc.mapper.RowMapper;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.entity.User.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        return new Builder(email)
                .id(id)
                .password(password)
                .build();
    }
}
