package ua.auction.bidme.service;

import ua.auction.bidme.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAll(int userId);
}
