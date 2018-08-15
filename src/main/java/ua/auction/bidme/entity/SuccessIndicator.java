package ua.auction.bidme.entity;

import static java.util.Arrays.stream;

public enum SuccessIndicator {

    SUCCESS("S"), FAIL("F");
    private String id;

    SuccessIndicator(String id) {
        this.id = id;
    }

    public static SuccessIndicator getById(String id) {
        return stream(SuccessIndicator.values())
                .filter(indicator -> indicator.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("unexpected indicator " + id));
    }

    public String getId() {
        return id;
    }

}
