
package ua.auction.bidme.dao;

import org.junit.Before;
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
import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, JdbcLotDao.class})
public class JdbcLotDaoTest {
    private Connection connection;
    private DataSource dataSource;
    private Statement statement;
    private ResultSet resultSet;
    private LotFilter lotFilter;
    private LotRowMapper lotRowMapper;
    private LotDao lotDao;
    PreparedStatement preparedStatement;
    private String GET_ALL_LOTS_SQL = "SELECT id, name, description, start_price, current_price, start_time," +
            "end_time,status, picture_link FROM auction.lot  LIMIT 0 OFFSET 0";
    private String GET_LOTS_COUNT = "SELECT COUNT(*) AS rowcount FROM auction.lot ";
    private String GET_LOTS_BY_ID_SQL = "SELECT id,name, description, start_price, current_price, start_time, end_time," +
            "status, picture_link FROM auction.lot WHERE id = ?";
    private String UPDATE_LOT_SQL = "UPDATE  auction.lot SET current_price=?, user_id=? WHERE id=? AND current_price=?";


    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        dataSource = mock(DataSource.class);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        lotFilter = mock(LotFilter.class);
        preparedStatement = mock(PreparedStatement.class);
        lotRowMapper = mock(LotRowMapper.class);
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        lotDao = new JdbcLotDao(dataSource);
    }

    @Test
    public void testGetAll() throws Exception {
        //before
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_ALL_LOTS_SQL)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        Lot lot = new Lot();
        when(lotRowMapper.mapRow(resultSet)).thenReturn(lot);

        //when
        List<Lot> lots = lotDao.getAll(lotFilter);

        //then
        assertEquals(1, lots.size());
        assertEquals(lot, lots.get(0));
    }

    @Test
    public void testGetPageCount() throws Exception {
        //    before
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_LOTS_COUNT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("rowcount")).thenReturn(6);
        when(lotFilter.getLotPerPage()).thenReturn(6);

        //when
        int pageCount = lotDao.getPageCount(lotFilter);

        //then
        assertEquals(1, pageCount);
    }

    @Test
    public void testGet() throws Exception {
        //    before
        when(connection.prepareStatement(GET_LOTS_BY_ID_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        Lot lot = new Lot();
        when(lotRowMapper.mapRow(resultSet)).thenReturn(lot);

        //when
        Lot result = lotDao.get(1);

        //then
        assertEquals(lot, result);
    }

    @Test
    public void testMakeBid() throws Exception {
        //    before
        when(connection.prepareStatement(UPDATE_LOT_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean result = lotDao.makeBid(1, 1, 1);

        //then
        assertTrue(result);
    }
}
