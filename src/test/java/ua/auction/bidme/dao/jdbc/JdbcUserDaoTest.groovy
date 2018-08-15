package ua.auction.bidme.dao.jdbc

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import ua.auction.bidme.dao.UserDao
import ua.auction.bidme.dao.jdbc.mapper.implementation.UserMapper
import ua.auction.bidme.entity.User

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

    private final String GET_USER_SQL = "SELECT u.id, u.email, u.password from auction.user as u WHERE u.email = ?;";

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
        when(connection.prepareStatement(GET_USER_SQL)).thenReturn(statement)
        doNothing().when(statement).setString(isA(Integer.class), isA(String.class))
        when(statement.executeQuery()).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        User user = generateUser()
        when(userMapper.mapRow(resultSet)).thenReturn(user)

        UserDao userDao = new JdbcUserDao(dataSource)
        User actual = userDao.get("email")

        //then
        assertNotNull(actual)


    }

    private User generateUser() {
        new User.Builder("email").password("pass").id(1).build()
    }

}
