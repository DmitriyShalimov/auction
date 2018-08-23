package ua.auction.bidme.service.listener;

import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.entity.SuccessIndicator;
import ua.auction.bidme.entity.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static ua.auction.bidme.entity.SuccessIndicator.FAIL;
import static ua.auction.bidme.entity.SuccessIndicator.SUCCESS;

public class BidListener {
    private Map<Integer, User> bidLeader = new HashMap<>();
    private MessageDao messageDao;
    private final static String LOWER_BID_MESSAGE = "Someone made a higher bid for %s%n";
    private final static String HIGHER_BID_MESSAGE = "Your bid for {%s%n} are leading now. Congratulations!";
    private final static String WIN_MESSAGE = "You are win lot {%s%n}. Congratulations!";

    public BidListener(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void notify(int lotId, String lotTitle, User user) {
        User secondBidUser = bidLeader.get(lotId);
        if (secondBidUser != null) {
            messageDao.add(createMessage(lotId, secondBidUser.getId(), FAIL, format(LOWER_BID_MESSAGE, lotTitle)));
        }
        messageDao.add(createMessage(lotId, user.getId(), SUCCESS, format(HIGHER_BID_MESSAGE, lotTitle)));
        bidLeader.put(lotId, user);
    }

    public void notifyWinner(Lot lot) {
        Optional.ofNullable(bidLeader.get(lot.getId()))
                .ifPresent(winner -> {
                    String message = format(WIN_MESSAGE, lot.getTitle());
                    int userId = winner.getId();
                    messageDao.add(createMessage(lot.getId(), userId, SUCCESS, message));
                });
    }

    private Message createMessage(int lotId, int userId, SuccessIndicator indicator, String message) {
        return new Message.Builder(message)
                .indicator(indicator)
                .dateTime(LocalDateTime.now())
                .userId(userId)
                .lotId(lotId)
                .build();
    }
}
