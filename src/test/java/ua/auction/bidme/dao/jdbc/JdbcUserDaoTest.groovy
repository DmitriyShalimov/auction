package ua.auction.bidme.dao.jdbc

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import ua.auction.bidme.dao.UserDao
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapper
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapperBase
import ua.auction.bidme.entity.User
import ua.auction.bidme.util.PropertyReader

import javax.sql.DataSource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

import static junit.framework.TestCase.assertNotNull
import static org.mockito.Matchers.isA
import static org.mockito.Mockito.*

@RunWith(PowerMockRunner.class)
@PrepareForTest([DriverManager.class, JdbcUserDao.class])
class JdbcUserDaoTest {
    private Properties queryProperties = new PropertyReader("properties/query.properties").readProperties()
    private final String GET_USER_BY_EMAIL_SQL = queryProperties.getProperty("GET_USER_BY_EMAIL_SQL")
    private final String GET_USER_BY_ID_SQL = queryProperties.getProperty("GET_USER_BY_ID_SQL")

    @Test
    void testGetUserByEmail() {
        //prepare
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        PreparedStatement statement = mock(PreparedStatement.class)
        ResultSet resultSet = mock(ResultSet.class)
        UserMapper userMapper = mock(UserMapper.class)

        //when
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.prepareStatement(GET_USER_BY_EMAIL_SQL)).thenReturn(statement)
        doNothing().when(statement).setString(isA(Integer.class), isA(String.class))
        when(statement.executeQuery()).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        User user = generateUser()
        when(userMapper.mapRow(resultSet)).thenReturn(user)

        UserDao userDao = new JdbcUserDao(dataSource, queryProperties)
        User actual = userDao.get("email")

        //then
        assertNotNull(actual)
    }

    @Test
    void testGetUserById() {
        //prepare
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        PreparedStatement statement = mock(PreparedStatement.class)
        ResultSet resultSet = mock(ResultSet.class)
        UserMapperBase userMapper = mock(UserMapperBase.class)

        //when
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.prepareStatement(GET_USER_BY_ID_SQL)).thenReturn(statement)
        doNothing().when(statement).setInt(isA(Integer.class), isA(Integer.class))
        when(statement.executeQuery()).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        User user = generateUser()
        when(userMapper.mapRow(resultSet)).thenReturn(user)

        UserDao userDao = new JdbcUserDao(dataSource, queryProperties)
        User actual = userDao.get(1)

        //then
        assertNotNull(actual)
    }

    private static User generateUser() {
        new User.Builder("email").password("pass").id(1).build()
    }

}
