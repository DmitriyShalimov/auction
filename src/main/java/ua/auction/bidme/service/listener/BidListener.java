package ua.auction.bidme.service.listener;

import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.entity.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static ua.auction.bidme.entity.SuccessIndicator.getById;

public class BidListener {
    private Map<String, User> bidLeader = new HashMap<>();
    private MessageDao messageDao;

    public BidListener(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void notify(int lotId, User user) {
        User secondBidUser = bidLeader.get(String.valueOf(lotId));
        if (secondBidUser != null) {
            messageDao.add(createNewMessage(lotId, secondBidUser.getId(), "F"));
        }
        messageDao.add(createNewMessage(lotId, user.getId(), "S"));
        bidLeader.put(String.valueOf(lotId), user);
    }

    private Message createNewMessage(int lotId, int userId, String indicator) {
        return new Message.Builder("A bid was placed on the lot with id" + lotId)
                .indicator(getById(indicator))
                .dateTime(LocalDateTime.now())
                .userId(userId)
                .lotId(lotId)
                .build();
    }
}
