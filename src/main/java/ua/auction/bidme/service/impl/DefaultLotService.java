package ua.auction.bidme.service.impl;

import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.LotStatus;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.filter.LotFilter;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.listener.BidListener;

import java.util.List;
import java.time.LocalDateTime;

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
        for (Lot lot : lots) {
            updateLot(lot);
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
        updateLot(lot);
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

    private void updateLot(Lot lot) {
//        if (lot.getStatus().equals(LotStatus.WAITING)) {
//            if (LocalDateTime.now().isBefore(lot.getStartTime())) {
//                lot.setStatus(LotStatus.ACTIVE);
//                lot.setCurrentPrice(lot.getStartPrice());
//            }
//        }else{
//            if (lot.getStatus().equals(LotStatus.ACTIVE)) {
//                if (LocalDateTime.now().isBefore(lot.getStartTime())) {
//                    lot.setStatus(LotStatus.FINISHED);
//                    bidListener.notifyViner();
//                }
    //    }

    }

}
