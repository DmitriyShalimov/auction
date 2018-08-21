package ua.auction.bidme.service.listener;

import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.entity.Lot;
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
        String text = "A bid was placed on the lot with id" + lotId;
        if (secondBidUser != null) {
            messageDao.add(createNewMessage(lotId, secondBidUser.getId(), "F", text));
        }
        messageDao.add(createNewMessage(lotId, user.getId(), "S", text));
        bidLeader.put(String.valueOf(lotId), user);
    }

    public void notifyWinner(Lot lot) {
        String text = "You are win lot with id =" + lot.getId();
        messageDao.add(createNewMessage(lot.getId(), bidLeader.get(String.valueOf(lot.getId())).getId(), "S", text));
    }

    private Message createNewMessage(int lotId, int userId, String indicator, String text) {
        return new Message.Builder(text)
                .indicator(getById(indicator))
                .dateTime(LocalDateTime.now())
                .userId(userId)
                .lotId(lotId)
                .build();
    }
}
