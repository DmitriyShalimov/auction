package ua.auction.bidme.service.listener;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.LotRowMapper;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.entity.User;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BidListenerTest {

    @Test
    public void testNotify() throws Exception {
        //before
        User user = new User.Builder("email").password("pass").id(1).build();
        MessageDao messageDao = mock(MessageDao.class);

        //when
        BidListener bidListener = new BidListener(messageDao);

        //then
        verify(messageDao, times(1)).add(any(Message.class));
    }
}