package ua.auction.bidme.dao.jdbc.mapper.implementation

import org.junit.Test
import ua.auction.bidme.entity.User

import java.sql.ResultSet

import static junit.framework.TestCase.assertEquals
import static junit.framework.TestCase.assertNotNull
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class UserMapperBaseTest {
    private UserMapperBase userMapper = new UserMapperBase();

    @Test
    void testMapRow() {
        //prepare
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("email")).thenReturn("user@email.com")
        when(resultSet.getString("password")).thenReturn("password1234")
        //when

        User user = userMapper.mapRow(resultSet)

        //then
        assertNotNull(user)
        assertEquals("user@email.com", user.getEmail())
        assertEquals("password1234", user.getPassword())
    }
}
