package ua.auction.bidme.dao.jdbc.mapper.implementation

import org.junit.Test
import ua.auction.bidme.entity.Message

import java.sql.ResultSet
import java.time.LocalDateTime

import static java.sql.Timestamp.valueOf
import static java.time.LocalDateTime.now
import static junit.framework.TestCase.assertEquals
import static junit.framework.TestCase.assertNotNull
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static ua.auction.bidme.entity.SuccessIndicator.SUCCESS

class MessageMapperTest {
    private MessageMapper mapper = new MessageMapper();

    @Test
    void TestMessageMapper() {
        //prepare
        LocalDateTime dateTime = now()
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("text")).thenReturn("message text")
        when(resultSet.getString("status")).thenReturn("S")
        when(resultSet.getTimestamp("date")).thenReturn(valueOf(dateTime))
        when(resultSet.getInt("lot_id")).thenReturn(1)

        //when

        Message message = mapper.mapRow(resultSet)

        //then
        assertNotNull(message)
        assertEquals("message text", message.getText())
        assertEquals(SUCCESS, message.getIndicator())
        assertEquals(dateTime, message.getDateTime())
        assertEquals(1, message.getLotId())
    }
}
