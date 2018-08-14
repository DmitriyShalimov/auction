package ua.auction.bidme.dao;

import ua.auction.bidme.entity.Lot;

import java.util.List;

public interface LotDao {
    List<Lot> getAll();
}
