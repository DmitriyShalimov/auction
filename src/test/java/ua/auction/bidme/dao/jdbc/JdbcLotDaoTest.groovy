package ua.auction.bidme.dao.jdbc

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import ua.auction.bidme.dao.LotDao
import ua.auction.bidme.dao.jdbc.mapper.implementation.LotRowMapper
import ua.auction.bidme.entity.Lot
import ua.auction.bidme.filter.LotFilter
import ua.auction.bidme.util.PropertyReader

import javax.sql.DataSource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@RunWith(PowerMockRunner.class)
@PrepareForTest([DriverManager.class, JdbcLotDao.class])
class JdbcLotDaoTest {


    Properties queryProperties = new PropertyReader("properties/query.properties").readProperties()
    private String GET_ALL_LOTS_SQL = "SELECT id, name, description, start_price, current_price, start_time,end_time,status, picture_link FROM auction.lot  LIMIT 0 OFFSET 0"
    private String GET_LOTS_COUNT = queryProperties.getProperty("GET_LOTS_COUNT")
    private String GET_LOTS_BY_ID_SQL = queryProperties.getProperty("GET_LOTS_BY_ID_SQL")
    private String UPDATE_LOT_SQL = queryProperties.getProperty("UPDATE_LOT_SQL")


    @Test
    void testGetAll() throws Exception {
        //before
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        Statement statement = mock(Statement.class)
        ResultSet resultSet = mock(ResultSet.class)
        LotFilter lotFilter = mock(LotFilter.class)
        LotRowMapper lotRowMapper = mock(LotRowMapper.class)
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper)
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.createStatement()).thenReturn(statement)
        LotDao lotDao = new JdbcLotDao(dataSource, queryProperties)
        when(connection.createStatement()).thenReturn(statement)
        when(statement.executeQuery(GET_ALL_LOTS_SQL)).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        Lot lot = new Lot()
        when(lotRowMapper.mapRow(resultSet)).thenReturn(lot)

        //when
        List<Lot> lots = lotDao.getAll(lotFilter)

        //then
        assertEquals(1, lots.size())
        assertEquals(lot, lots.get(0))
    }

    @Test
    void testGetPageCount() throws Exception {
        //    before
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        Statement statement = mock(Statement.class)
        ResultSet resultSet = mock(ResultSet.class)
        LotFilter lotFilter = mock(LotFilter.class)
        LotRowMapper lotRowMapper = mock(LotRowMapper.class)
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper)
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.createStatement()).thenReturn(statement)
        LotDao lotDao = new JdbcLotDao(dataSource, queryProperties)
        when(connection.createStatement()).thenReturn(statement)
        when(statement.executeQuery(GET_LOTS_COUNT)).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getInt("rowcount")).thenReturn(6)
        when(lotFilter.getLotPerPage()).thenReturn(6)

        //when
        int pageCount = lotDao.getPageCount(lotFilter)

        //then
        assertEquals(1, pageCount)
    }

    @Test
    void testGetLotById() throws Exception {
        //    before
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        Statement statement = mock(Statement.class)
        ResultSet resultSet = mock(ResultSet.class)
        LotRowMapper lotRowMapper = mock(LotRowMapper.class)
        PreparedStatement preparedStatement = mock(PreparedStatement.class)
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper)
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.createStatement()).thenReturn(statement)
        LotDao lotDao = new JdbcLotDao(dataSource, queryProperties)
        when(connection.prepareStatement(GET_LOTS_BY_ID_SQL)).thenReturn(preparedStatement)
        when(preparedStatement.executeQuery()).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        Lot lot = new Lot()
        when(lotRowMapper.mapRow(resultSet)).thenReturn(lot)

        //when
        Lot result = lotDao.get(1)

        //then
        assertEquals(lot, result)
    }

    @Test
    void testMakeBid() throws Exception {
        //    before
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        Statement statement = mock(Statement.class)
        ResultSet resultSet = mock(ResultSet.class)
        LotRowMapper lotRowMapper = mock(LotRowMapper.class)
        PreparedStatement preparedStatement = mock(PreparedStatement.class)
        PowerMockito.whenNew(LotRowMapper.class).withNoArguments().thenReturn(lotRowMapper)
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.createStatement()).thenReturn(statement)
        LotDao lotDao = new JdbcLotDao(dataSource, queryProperties)
        when(connection.prepareStatement(UPDATE_LOT_SQL)).thenReturn(preparedStatement)
        when(preparedStatement.executeQuery()).thenReturn(resultSet)
        when(preparedStatement.executeUpdate()).thenReturn(1)

        //when
        boolean result = lotDao.makeBid(1, 1, 1)

        //then
        assertEquals(true, result)
    }
}
