package ua.auction.bidme.dao.jdbc.mapper.implementation;

import ua.auction.bidme.entity.User;
import ua.auction.bidme.entity.User.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper extends UserMapperBase {
    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = super.mapRow(resultSet);
        int id = resultSet.getInt("id");
        return new Builder(user.getEmail())
                .id(id)
                .password(user.getPassword())
                .build();
    }
}
