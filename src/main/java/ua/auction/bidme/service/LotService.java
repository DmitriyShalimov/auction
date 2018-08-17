package ua.auction.bidme.service;

import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.filter.LotFilter;

import java.util.List;

public interface LotService {
    List<Lot> getAll(LotFilter lotFilter);

    int getPageCount(LotFilter lotFilter);

    Lot get(int id);

    boolean makeBid(int lotId, int price, User user);
}
