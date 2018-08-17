package ua.auction.bidme.dao.jdbc

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import ua.auction.bidme.dao.MessageDao
import ua.auction.bidme.dao.jdbc.mapper.implementation.MessageMapper
import ua.auction.bidme.entity.Message

import javax.sql.DataSource
import java.sql.*
import java.time.LocalDateTime

import static java.sql.Timestamp.valueOf
import static java.time.LocalDateTime.now
import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.isA
import static org.mockito.Mockito.*
import static ua.auction.bidme.entity.SuccessIndicator.FAIL
import static ua.auction.bidme.entity.SuccessIndicator.SUCCESS

@RunWith(PowerMockRunner.class)
@PrepareForTest([DriverManager.class, JdbcMessageDao.class])
class JdbcMessageDaoTest {
    private final String GET_MESSAGES_BY_USER_ID_SQL = "SELECT m.id, m.text, m.status, m.date, l.id as lot_id, l.name as lotTitle" +
            " FROM auction.message as m " +
            "INNER JOIN auction.lot as l on (m.lotId = l.id)  WHERE m.userId = ?";

    @Test
    void testGetAllByUserId() {
        Connection connection = mock(Connection.class)
        DataSource dataSource = mock(DataSource.class)
        PreparedStatement statement = mock(PreparedStatement.class)
        ResultSet resultSet = mock(ResultSet.class)
        MessageMapper mapper = mock(MessageMapper.class)
        Timestamp timestamp = mock(Timestamp.class)
        LocalDateTime now = now()
        List<Message> messages = generateMessages()

        //when
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.prepareStatement(GET_MESSAGES_BY_USER_ID_SQL)).thenReturn(statement)
        doNothing().when(statement).setString(isA(Integer.class), isA(String.class))
        when(statement.executeQuery()).thenReturn(resultSet)
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false)
        when(resultSet.getString("status")).thenReturn(SUCCESS.getId()).thenReturn(FAIL.getId()).thenReturn(SUCCESS.getId())
        when(resultSet.getTimestamp("date")).thenReturn(valueOf(now)).thenReturn(valueOf(now)).thenReturn(valueOf(now))
        when(timestamp.toLocalDateTime()).thenReturn(now).thenReturn(now).thenReturn(now)
        when(mapper.mapRow(resultSet)).thenReturn(messages.get(0)).thenReturn(messages.get(1)).thenReturn(messages.get(2))

        MessageDao messageDao = new JdbcMessageDao(dataSource)
        //then
        List<Message> actual = messageDao.getAll(1)

        assertEquals(messages.size(), actual.size())
    }

    private static List<Message> generateMessages() {
        List<Message> messages = new ArrayList<>()
        (0..2).eachWithIndex {
            int entry, int i -> messages.add(generateMessage(i))
        }
        return messages
    }

    private static Message generateMessage(int i) {
        LocalDateTime now = now()
        return new Message.Builder("text " + i)
                .id(i)
                .dateTime(now)
                .indicator(i % 2 ? SUCCESS : FAIL)
                .build()
    }

}
