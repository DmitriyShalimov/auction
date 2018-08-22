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

    public void notify(int lotId, String lotTitle, User user) {
        User secondBidUser = bidLeader.get(String.valueOf(lotId));
        if (secondBidUser != null) {
            messageDao.add(createNewMessage(lotId, secondBidUser.getId(), "F", "Someone made a higher bid for " + lotTitle));
        }
        messageDao.add(createNewMessage(lotId, user.getId(), "S", "Your bid for " + lotTitle + " are leading now. Congratulations!"));
        bidLeader.put(String.valueOf(lotId), user);
    }

    public void notifyWinner(Lot lot) {
        String text = "You are win lot " + lot.getTitle() + ". Congratulations!";
        User winner = bidLeader.get(String.valueOf(lot.getId()));
        if (winner != null) {
            messageDao.add(createNewMessage(lot.getId(), bidLeader.get(String.valueOf(lot.getId())).getId(), "S", text));
        }
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
