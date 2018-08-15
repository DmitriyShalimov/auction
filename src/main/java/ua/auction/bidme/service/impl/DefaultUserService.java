package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.service.UserService;

public class DefaultUserService implements UserService {

    private final UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User get(String email) {
        return userDao.get(email);
    }
}
