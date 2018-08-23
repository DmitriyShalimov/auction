package ua.auction.bidme.service;

import ua.auction.bidme.entity.User;
import ua.auction.bidme.entity.UserData;

public interface UserService {
    User get(String email);

    UserData get(int id);
}
