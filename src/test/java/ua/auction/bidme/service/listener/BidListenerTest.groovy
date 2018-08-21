package ua.auction.bidme.service.listener

import org.junit.Test
import ua.auction.bidme.dao.MessageDao
import ua.auction.bidme.entity.Message
import ua.auction.bidme.entity.User

import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

class BidListenerTest {

    @Test
    void testNotify() throws Exception {
        //before
        User user = new User.Builder("email").password("pass").id(1).build()
        MessageDao messageDao = mock(MessageDao.class)

        //when
        BidListener bidListener = new BidListener(messageDao)
        bidListener.notify(1, user)

        //then
        verify(messageDao, times(1)).add(any(Message.class))
    }
}