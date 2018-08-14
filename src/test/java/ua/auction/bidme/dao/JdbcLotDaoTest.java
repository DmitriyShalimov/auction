package ua.auction.bidme.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.auction.bidme.dao.jdbc.JdbcLotDao;
import ua.auction.bidme.dao.jdbc.mapper.LotRowMapper;
import ua.auction.bidme.entity.Lot;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, JdbcLotDao.class})
public class JdbcLotDaoTest {

    @Test
    public void testGetAll() throws Exception {
        //    before
        Connection connection = Mockito.mock(Connection.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Statement statement = Mockito.mock(Statement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        LotRowMapper lotRowMapper = Mockito.mock(LotRowMapper.class);
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT name, description, start_price, current_price, start_time, end_time,status, picture_link FROM \"Lots\" ")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        Lot lot = new Lot();
        when(lotRowMapper.mapRow(resultSet)).thenReturn(lot);

        //when
        LotDao lotDao = new JdbcLotDao();
        ((JdbcLotDao) lotDao).setDataSource(dataSource);
        List<Lot> lots = lotDao.getAll();

        //then
        assertEquals(1, lots.size());
        assertEquals(lot, lots.get(0));

    }
}
