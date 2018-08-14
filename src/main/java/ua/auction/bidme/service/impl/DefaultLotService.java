package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.service.LotService;

import java.util.List;

public class DefaultLotService implements LotService {

    private LotDao lotDao;

    @Override
    public List<Lot> getAll() {
        return lotDao.getAll();
    }

    public void setLotDao(LotDao lotDao) {
        this.lotDao = lotDao;
    }
}
