package ua.auction.bidme.dao.jdbc.mapper.implementation;

import org.junit.Test;
import org.mockito.Mockito;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.LotStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.sql.Timestamp.valueOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class LotRowMapperTest {

    @Test
    public void testMapRow() throws SQLException {
        //    before
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(resultSet.getString("name")).thenReturn("name");
        when(resultSet.getInt("start_price")).thenReturn(1);
        when(resultSet.getInt("current_price")).thenReturn(2);
        when(resultSet.getString("description")).thenReturn("description");
        when(resultSet.getTimestamp("start_time")).thenReturn(valueOf("2016-01-01 12:10:04"));
        when(resultSet.getTimestamp("end_time")).thenReturn(valueOf("2016-01-02 12:10:04"));
        when(resultSet.getString("picture_link")).thenReturn("image");
        when(resultSet.getString("status")).thenReturn("A");
        //when
        LotRowMapper LotRowMapper = new LotRowMapper();
        Lot lot = LotRowMapper.mapRow(resultSet);

        //then
        assertEquals("name", lot.getTitle());
        assertEquals(1, lot.getStartPrice());
        assertEquals(2, lot.getCurrentPrice());
        assertEquals("description", lot.getDescription());
        assertEquals("image", lot.getImage());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertEquals(LocalDateTime.parse("2016-01-01 12:10:04", formatter), lot.getStartTime());
        assertEquals(LocalDateTime.parse("2016-01-02 12:10:04", formatter), lot.getEndTime());
        assertEquals(LotStatus.ACTIVE, lot.getStatus());
    }
}
