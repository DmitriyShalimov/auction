package ua.auction.bidme.service.listener;

import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.entity.SuccessIndicator;
import ua.auction.bidme.entity.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static ua.auction.bidme.entity.SuccessIndicator.SUCCESS;
import static ua.auction.bidme.entity.SuccessIndicator.getById;

public class BidListener {
    private Map<Integer, User> bidLeader = new HashMap<>();
    private MessageDao messageDao;
    private final static String LOWER_BID_MESSAGE = "Someone made a higher bid for %s%n";
    private final static String HIGHER_BID_MESSAGE = "Your bid for %s%n are leading now. Congratulations!";
    private final static String WIN_MESSAGE = "You are win lot %s%n. Congratulations!";

    public BidListener(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void notify(int lotId, String lotTitle, User user) {
        User secondBidUser = bidLeader.get(lotId);
        if (secondBidUser != null) {
            messageDao.add(createNewMessage(lotId, secondBidUser.getId(), SuccessIndicator.FAIL.getId(), String.format(LOWER_BID_MESSAGE, lotTitle)));
        }
        messageDao.add(createNewMessage(lotId, user.getId(), SUCCESS.getId(), String.format(HIGHER_BID_MESSAGE, lotTitle)));
        bidLeader.put(lotId, user);
    }

    public void notifyWinner(Lot lot) {
        String text = String.format(WIN_MESSAGE, lot.getTitle());
        User winner = bidLeader.get(lot.getId());
        if (winner != null) {
            messageDao.add(createNewMessage(lot.getId(), bidLeader.get(lot.getId()).getId(), SUCCESS.getId(), text));
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
