package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.service.MessageService;
import ua.auction.bidme.service.UserService;

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

}
