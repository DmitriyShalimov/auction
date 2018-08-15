package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.service.MessageService;

import java.util.List;

public class DefaultMessageService implements MessageService {
    private MessageDao messageDao;

    public DefaultMessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<Message> getAll(int userId) {
        return messageDao.getAll(userId);
    }
}
