package ua.auction.bidme.dao.jdbc.mapper.implementation;

import ua.auction.bidme.dao.jdbc.mapper.RowMapper;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.LotStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LotRowMapper implements RowMapper<Lot> {
    public Lot mapRow(ResultSet resultSet) throws SQLException {
        Lot lot = new Lot();
        lot.setTitle(resultSet.getString("name"));
        lot.setStartPrice(resultSet.getInt("start_price"));
        lot.setDescription(resultSet.getString("description"));
        lot.setImage(resultSet.getString("picture_link"));
        lot.setCurrentPrice(resultSet.getInt("current_price"));
        lot.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
        lot.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
        lot.setStatus(LotStatus.getTypeById(resultSet.getString("status")));
        return lot;
    }
}
