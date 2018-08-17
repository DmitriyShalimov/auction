package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.entity.Message;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.entity.UserData;
import ua.auction.bidme.service.MessageService;
import ua.auction.bidme.service.UserService;

import java.util.List;

public class DefaultUserService implements UserService {

    private final UserDao userDao;
    private final MessageService messageService;

    public DefaultUserService(UserDao userDao, MessageService messageService) {
        this.userDao = userDao;
        this.messageService = messageService;
    }

    @Override
    public User get(String email) {
        return userDao.get(email);
    }

    @Override
    public UserData get(int id) {
        User user = userDao.get(id);
        List<Message> messages = messageService.getAll(id);
        return new UserData(user, messages);
    }

}
