package ua.auction.bidme.service.security;

import ua.auction.bidme.entity.User;

import java.util.HashMap;
import java.util.Map;

public class LoggedUserStorage {
    private Map<String, User> storage;

    public LoggedUserStorage() {
        storage = new HashMap<>();
    }

    public boolean log(String sessionId, User user) {
        return storage.put(sessionId, user) != null;
    }

    public User getLoggedUser(String sessionId) {
        return storage.get(sessionId);
    }

}
