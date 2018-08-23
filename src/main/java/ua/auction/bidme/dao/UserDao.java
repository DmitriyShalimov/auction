package ua.auction.bidme.dao;

import ua.auction.bidme.entity.User;

public interface UserDao {
    User get(String email);

    User get(int id);
}
