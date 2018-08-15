package ua.auction.bidme.dao;

import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.filter.LotFilter;

import java.util.List;

public interface LotDao {
    List<Lot> getAll(LotFilter lotFilter);

    int getPageCount(LotFilter lotFilter);
}
