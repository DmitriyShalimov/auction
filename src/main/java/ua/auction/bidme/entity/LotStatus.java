package ua.auction.bidme.entity;

public enum LotStatus {
    ACTIVE("A"), FINISHED("F"), WAITING("W");
    private String id;

    LotStatus(String id) {
        this.id = id;
    }

    public static LotStatus getTypeById(String id) {
        for (LotStatus lotType : LotStatus.values()) {
            if (lotType.getId().equals(id)) {
                return lotType;
            }
        }
        throw new IllegalArgumentException("No lotType found for id = " + id);
    }

    public String getId() {
        return id;
    }
}
