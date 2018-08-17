package ua.auction.bidme.dao.jdbc.mapper.implementation;

import ua.auction.bidme.dao.jdbc.mapper.RowMapper;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.entity.SuccessIndicator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static ua.auction.bidme.entity.SuccessIndicator.getById;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet resultSet) throws SQLException {
        String text = resultSet.getString("text");
        SuccessIndicator indicator = getById(resultSet.getString("status"));
        LocalDateTime dateTime = resultSet.getTimestamp(("date")).toLocalDateTime();
        int lotId = resultSet.getInt("lot_id");
        return new Message.Builder(text)
                .indicator(indicator)
                .dateTime(dateTime)
                .lotId(lotId)
                .build();
    }
}
