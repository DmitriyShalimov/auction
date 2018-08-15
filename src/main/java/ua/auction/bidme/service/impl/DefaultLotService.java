package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.filter.LotFilter;
import ua.auction.bidme.service.LotService;

import java.util.List;

public class DefaultLotService implements LotService {

    private final LotDao lotDao;

    public DefaultLotService(LotDao lotDao) {
        this.lotDao = lotDao;
    }

    @Override
    public List<Lot> getAll(LotFilter lotFilter) {
        return lotDao.getAll(lotFilter);
    }

    @Override
    public int getPageCount(LotFilter lotFilter) {
        return lotDao.getPageCount(lotFilter);
    }

}
