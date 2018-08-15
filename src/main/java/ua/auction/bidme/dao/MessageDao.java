package ua.auction.bidme.dao;

import ua.auction.bidme.entity.Message;

import java.util.List;

public interface MessageDao {

    List<Message> getAll(int userId);

}
