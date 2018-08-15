package ua.auction.bidme.filter;

import ua.auction.bidme.entity.LotStatus;

public class LotFilter {
    private LotStatus lotStatus;
    private int page;
    private int lotPerPage;

    public int getLotPerPage() {
        return lotPerPage;
    }

    public void setLotPerPage(int lotPerPage) {
        this.lotPerPage = lotPerPage;
    }

    public LotStatus getLotStatus() {
        return lotStatus;
    }

    public void setLotStatus(LotStatus lotStatus) {
        this.lotStatus = lotStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
