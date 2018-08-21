package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.LotStatus;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.filter.LotFilter;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.listener.BidListener;

import java.util.List;

import static java.time.LocalDateTime.now;
import static ua.auction.bidme.entity.LotStatus.ACTIVE;

public class DefaultLotService implements LotService {

    private final LotDao lotDao;
    private final BidListener bidListener;

    public DefaultLotService(LotDao lotDao, BidListener bidListener) {
        this.lotDao = lotDao;
        this.bidListener = bidListener;
    }

    @Override
    public List<Lot> getAll(LotFilter lotFilter) {
        List<Lot> lots = lotDao.getAll(lotFilter);
        boolean isUpdate = false;
        for (Lot lot : lots) {
            if (updateLot(lot)) {
                isUpdate = true;
            }
        }
        if (isUpdate) {
            return lotDao.getAll(lotFilter);
        }
        return lots;
    }

    @Override
    public int getPageCount(LotFilter lotFilter) {
        return lotDao.getPageCount(lotFilter);
    }

    @Override
    public Lot get(int id) {
        Lot lot = lotDao.get(id);
        boolean isUpdate = updateLot(lot);
        if (isUpdate) {
            return lotDao.get(id);
        }
        return lot;
    }

    @Override
    public boolean makeBid(int lotId, int price, User user) {
        boolean result = lotDao.makeBid(lotId, price, user.getId());
        if (result) {
            bidListener.notify(lotId, user);
        }
        return result;
    }

    private boolean updateLot(Lot lot) {
        boolean isUpdated = false;
        if (lot.getStatus().equals(LotStatus.WAITING) && lot.getStartTime().isBefore(now())) {
            lot.setStatus(ACTIVE);
            lot.setCurrentPrice(lot.getStartPrice());
            isUpdated = lotDao.update(lot);
        } else {
            if (lot.getStatus().equals(ACTIVE) && lot.getEndTime().isBefore(now())) {
                lot.setStatus(LotStatus.FINISHED);
                bidListener.notifyWinner(lot);
                isUpdated = lotDao.update(lot);
            }
        }
        return isUpdated;
    }
}
