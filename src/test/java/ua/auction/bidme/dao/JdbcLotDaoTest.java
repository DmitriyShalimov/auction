
package ua.auction.bidme.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.auction.bidme.dao.jdbc.JdbcLotDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.LotRowMapper;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.filter.LotFilter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, JdbcLotDao.class})
public class JdbcLotDaoTest {

    private String sql = "SELECT id, name, description, start_price, current_price, start_time,end_time,status, picture_link FROM auction.lot  LIMIT 0 OFFSET 0";

    @Test
    public void testGetAll() throws Exception {
        //    before
        Connection connection = mock(Connection.class);
        DataSource dataSource = mock(DataSource.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        LotFilter lotFilter = mock(LotFilter.class);
        LotRowMapper lotRowMapper = mock(LotRowMapper.class);
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(sql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        Lot lot = new Lot();
        when(lotRowMapper.mapRow(resultSet)).thenReturn(lot);

        //when
        LotDao lotDao = new JdbcLotDao(dataSource);
        List<Lot> lots = lotDao.getAll(lotFilter);

        //then
        assertEquals(1, lots.size());
        assertEquals(lot, lots.get(0));

    }
}
